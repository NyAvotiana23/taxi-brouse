package com.mdgtaxi.util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour les opérations d'agrégation génériques sur des listes et des entités JPA
 */
public class TableUtil {

    // ===============================================
    // OPERATIONS SUR LISTES (avec Réflexion)
    // ===============================================

    /**
     * Calcule la somme d'un attribut numérique sur une liste d'objets
     * @param objects Liste d'objets
     * @param attributeName Nom de l'attribut à sommer
     * @return La somme
     */
    public static double sum(List<?> objects, String attributeName) {
        if (objects == null || objects.isEmpty()) {
            return 0.0;
        }

        return objects.stream()
                .map(obj -> getNumericValue(obj, attributeName))
                .filter(Objects::nonNull)
                .mapToDouble(Number::doubleValue)
                .sum();
    }

    /**
     * Calcule la somme de plusieurs attributs
     * @param objects Liste d'objets
     * @param attributeNames Noms des attributs à sommer
     * @return Map avec attribut -> somme
     */
    public static Map<String, Double> sumMultiple(List<?> objects, String... attributeNames) {
        Map<String, Double> results = new HashMap<>();

        for (String attributeName : attributeNames) {
            results.put(attributeName, sum(objects, attributeName));
        }

        return results;
    }

    /**
     * Calcule la moyenne d'un attribut
     */
    public static double average(List<?> objects, String attributeName) {
        if (objects == null || objects.isEmpty()) {
            return 0.0;
        }

        return objects.stream()
                .map(obj -> getNumericValue(obj, attributeName))
                .filter(Objects::nonNull)
                .mapToDouble(Number::doubleValue)
                .average()
                .orElse(0.0);
    }

    /**
     * Calcule la moyenne de plusieurs attributs
     */
    public static Map<String, Double> averageMultiple(List<?> objects, String... attributeNames) {
        Map<String, Double> results = new HashMap<>();

        for (String attributeName : attributeNames) {
            results.put(attributeName, average(objects, attributeName));
        }

        return results;
    }

    /**
     * Trouve la valeur minimale d'un attribut
     */
    public static double min(List<?> objects, String attributeName) {
        if (objects == null || objects.isEmpty()) {
            return 0.0;
        }

        return objects.stream()
                .map(obj -> getNumericValue(obj, attributeName))
                .filter(Objects::nonNull)
                .mapToDouble(Number::doubleValue)
                .min()
                .orElse(0.0);
    }

    /**
     * Trouve la valeur maximale d'un attribut
     */
    public static double max(List<?> objects, String attributeName) {
        if (objects == null || objects.isEmpty()) {
            return 0.0;
        }

        return objects.stream()
                .map(obj -> getNumericValue(obj, attributeName))
                .filter(Objects::nonNull)
                .mapToDouble(Number::doubleValue)
                .max()
                .orElse(0.0);
    }

    /**
     * Compte le nombre d'objets
     */
    public static long count(List<?> objects) {
        return objects == null ? 0 : objects.size();
    }

    /**
     * Compte le nombre d'objets ayant une valeur non-nulle pour un attribut
     */
    public static long countNotNull(List<?> objects, String attributeName) {
        if (objects == null || objects.isEmpty()) {
            return 0;
        }

        return objects.stream()
                .map(obj -> getValue(obj, attributeName))
                .filter(Objects::nonNull)
                .count();
    }

    /**
     * Groupe les objets par valeur d'un attribut et compte
     */
    public static <T> Map<Object, Long> groupByAndCount(List<T> objects, String attributeName) {
        if (objects == null || objects.isEmpty()) {
            return new HashMap<>();
        }

        return objects.stream()
                .collect(Collectors.groupingBy(
                        obj -> getValue(obj, attributeName),
                        Collectors.counting()
                ));
    }

    /**
     * Groupe les objets par valeur d'un attribut et somme un autre attribut
     */
    public static <T> Map<Object, Double> groupByAndSum(List<T> objects, String groupByAttribute, String sumAttribute) {
        if (objects == null || objects.isEmpty()) {
            return new HashMap<>();
        }

        return objects.stream()
                .collect(Collectors.groupingBy(
                        obj -> getValue(obj, groupByAttribute),
                        Collectors.summingDouble(obj -> {
                            Number value = getNumericValue(obj, sumAttribute);
                            return value != null ? value.doubleValue() : 0.0;
                        })
                ));
    }

    /**
     * Agrégations multiples sur une liste
     */
    public static Map<String, Object> aggregate(List<?> objects, String attributeName) {
        Map<String, Object> results = new HashMap<>();
        results.put("sum", sum(objects, attributeName));
        results.put("average", average(objects, attributeName));
        results.put("min", min(objects, attributeName));
        results.put("max", max(objects, attributeName));
        results.put("count", countNotNull(objects, attributeName));
        return results;
    }

    // ===============================================
    // OPERATIONS AVEC REQUETES JPA
    // ===============================================

    /**
     * Exécute une somme directement avec une requête JPA
     */
    public static <T> Double sumFromQuery(EntityManager em, Class<T> entityClass, String attributeName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<T> root = cq.from(entityClass);

        cq.select(cb.sum(root.get(attributeName)));

        Double result = em.createQuery(cq).getSingleResult();
        return result != null ? result : 0.0;
    }

    /**
     * Exécute une somme avec condition
     */
    public static <T> Double sumFromQueryWithCondition(EntityManager em, Class<T> entityClass,
                                                       String attributeName, String conditionAttribute, Object conditionValue) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<T> root = cq.from(entityClass);

        cq.select(cb.sum(root.get(attributeName)))
                .where(cb.equal(root.get(conditionAttribute), conditionValue));

        Double result = em.createQuery(cq).getSingleResult();
        return result != null ? result : 0.0;
    }

    /**
     * Compte avec une requête JPA
     */
    public static <T> Long countFromQuery(EntityManager em, Class<T> entityClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);

        cq.select(cb.count(root));

        return em.createQuery(cq).getSingleResult();
    }

    /**
     * Moyenne avec une requête JPA
     */
    public static <T> Double averageFromQuery(EntityManager em, Class<T> entityClass, String attributeName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<T> root = cq.from(entityClass);

        cq.select(cb.avg(root.get(attributeName)));

        Double result = em.createQuery(cq).getSingleResult();
        return result != null ? result : 0.0;
    }

    /**
     * Min avec une requête JPA
     */
    public static <T> Double minFromQuery(EntityManager em, Class<T> entityClass, String attributeName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<T> root = cq.from(entityClass);

        Expression<Double> minExpression = cb.min(root.get(attributeName));
        cq.select(minExpression);

        Double result = em.createQuery(cq).getSingleResult();
        return result != null ? result : 0.0;
    }

    /**
     * Max avec une requête JPA
     */
    public static <T> Double maxFromQuery(EntityManager em, Class<T> entityClass, String attributeName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<T> root = cq.from(entityClass);

        Expression<Double> maxExpression = cb.max(root.get(attributeName));
        cq.select(maxExpression);

        Double result = em.createQuery(cq).getSingleResult();
        return result != null ? result : 0.0;
    }

    /**
     * GROUP BY avec COUNT en requête JPA
     */
    public static <T> List<Object[]> groupByAndCountFromQuery(EntityManager em, Class<T> entityClass, String groupByAttribute) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<T> root = cq.from(entityClass);

        cq.multiselect(root.get(groupByAttribute), cb.count(root))
                .groupBy(root.get(groupByAttribute));

        return em.createQuery(cq).getResultList();
    }

    /**
     * GROUP BY avec SUM en requête JPA
     */
    public static <T> List<Object[]> groupByAndSumFromQuery(EntityManager em, Class<T> entityClass,
                                                            String groupByAttribute, String sumAttribute) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<T> root = cq.from(entityClass);

        cq.multiselect(root.get(groupByAttribute), cb.sum(root.get(sumAttribute)))
                .groupBy(root.get(groupByAttribute));

        return em.createQuery(cq).getResultList();
    }

    /**
     * Agrégations multiples avec requête JPA
     */
    public static <T> Map<String, Object> aggregateFromQuery(EntityManager em, Class<T> entityClass, String attributeName) {
        Map<String, Object> results = new HashMap<>();
        results.put("sum", sumFromQuery(em, entityClass, attributeName));
        results.put("average", averageFromQuery(em, entityClass, attributeName));
        results.put("count", countFromQuery(em, entityClass));
        return results;
    }

    /**
     * Exécute une requête JPQL personnalisée pour agrégation
     */
    public static Object executeAggregateQuery(EntityManager em, String jpql) {
        Query query = em.createQuery(jpql);
        return query.getSingleResult();
    }

    /**
     * Exécute une requête SQL native pour agrégation
     */
    public static Object executeNativeAggregateQuery(EntityManager em, String sql) {
        Query query = em.createNativeQuery(sql);
        return query.getSingleResult();
    }

    // ===============================================
    // METHODES UTILITAIRES PRIVEES (Réflexion)
    // ===============================================

    private static Object getValue(Object obj, String attributeName) {
        try {
            Field field = findField(obj.getClass(), attributeName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'accès à l'attribut: " + attributeName, e);
        }
    }

    private static Number getNumericValue(Object obj, String attributeName) {
        Object value = getValue(obj, attributeName);

        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return (Number) value;
        }

        throw new IllegalArgumentException("L'attribut " + attributeName + " n'est pas numérique");
    }

    private static Field findField(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        throw new RuntimeException("Champ non trouvé: " + fieldName + " dans la classe " + clazz.getName());
    }
}