<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container-fluid">
    <h2 class="mb-4"><i class="bi bi-speedometer2"></i> Dashboard</h2>

    <div class="row g-4">
        <div class="col-md-6">
            <div class="card border-primary">
                <div class="card-body text-center">
                    <i class="bi bi-person-fill text-primary" style="font-size: 3rem;"></i>
                    <h3 class="mt-3">Drivers Management</h3>
                    <p class="text-muted">Manage all your taxi drivers</p>
                    <a href="${pageContext.request.contextPath}/driver" class="btn btn-primary">
                        <i class="bi bi-arrow-right-circle"></i> Go to Drivers
                    </a>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card border-success">
                <div class="card-body text-center">
                    <i class="bi bi-car-front-fill text-success" style="font-size: 3rem;"></i>
                    <h3 class="mt-3">Vehicles Management</h3>
                    <p class="text-muted">Manage all your taxi vehicles</p>
                    <a href="${pageContext.request.contextPath}/vehicle" class="btn btn-success">
                        <i class="bi bi-arrow-right-circle"></i> Go to Vehicles
                    </a>
                </div>
            </div>
        </div>
    </div>

    <div class="row mt-4">
        <div class="col-12">
            <div class="card bg-light">
                <div class="card-body">
                    <h5><i class="bi bi-info-circle"></i> Welcome to Taxi Brousse Management System</h5>
                    <p class="mb-0">Use the sidebar to navigate between different sections of the application.</p>
                </div>
            </div>
        </div>
    </div>
</div>