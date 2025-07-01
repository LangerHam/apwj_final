<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- THIS LINE HAS BEEN CHANGED TO THE NEW JAKARTA URI --%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>SuperShop Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
    <h1>SuperShop Management</h1>
    <hr/>

    <div class="row">
        <div class="col-md-6">
            <h3>Add Product</h3>
            <form action="products/add" method="post">
                <div class="mb-3"><input type="text" name="name" class="form-control" placeholder="Product Name" required></div>
                <div class="mb-3">
                    <select name="category" class="form-select" required>
                        <option value="BEAUTY_CARE">Beauty Care</option>
                        <option value="VEGETABLES">Vegetables</option>
                        <option value="MEAT">Meat</option>
                        <option value="GROCERIES">Groceries</option>
                    </select>
                </div>
                <div class="mb-3"><input type="number" step="0.01" name="price" class="form-control" placeholder="Price" required></div>
                <div class="mb-3"><input type="date" name="expiryDate" class="form-control" required></div>
                <button type="submit" class="btn btn-primary">Add Product</button>
            </form>
        </div>
        <div class="col-md-6">
            <h3>Modify Product</h3>
            <form action="products/modify" method="post">
                <div class="mb-3"><input type="number" name="id" class="form-control" placeholder="Product ID to Modify" required></div>
                <div class="mb-3"><input type="text" name="name" class="form-control" placeholder="New Name"></div>
                 <div class="mb-3">
                    <select name="category" class="form-select">
                        <option value="">-- New Category --</option>
                        <option value="BEAUTY_CARE">Beauty Care</option>
                        <option value="VEGETABLES">Vegetables</option>
                        <option value="MEAT">Meat</option>
                        <option value="GROCERIES">Groceries</option>
                    </select>
                </div>
                <div class="mb-3"><input type="number" step="0.01" name="price" class="form-control" placeholder="New Price"></div>
                <div class="mb-3"><input type="date" name="expiryDate" class="form-control"></div>
                <button type="submit" class="btn btn-warning">Modify Product</button>
            </form>
        </div>
    </div>
    <hr/>

    <h3>View Operations</h3>
    <div class="my-3">
        <a href="${pageContext.request.contextPath}/products/expiring" class="btn btn-info">Show Products Expiring in 7 Days</a>
        <a href="${pageContext.request.contextPath}/products/total-price" class="btn btn-info">Show Total Price by Category</a>
    </div>

     <div class="my-3">
        <form action="${pageContext.request.contextPath}/products/expiring/discounted" method="post" class="d-flex">
             <input type="number" name="discountPercentage" class="form-control me-2" placeholder="Discount %" required>
             <button type="submit" class="btn btn-success">Apply Discount</button>
        </form>
    </div>
    <hr/>

    <div class="row">
        <c:if test="${not empty expiringProducts}">
            <div class="col-md-12">
                <h3>Products Expiring in 7 Days</h3>
                <table class="table table-striped">
                    <thead><tr><th>ID</th><th>Name</th><th>Category</th><th>Price</th><th>Expiry Date</th></tr></thead>
                    <tbody>
                    <c:forEach var="p" items="${expiringProducts}">
                        <tr>
                            <td><c:out value="${p.id}"/></td>
                            <td><c:out value="${p.name}"/></td>
                            <td><c:out value="${p.category}"/></td>
                            <td><c:out value="${p.price}"/></td>
                            <td><c:out value="${p.expiryDate}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <c:if test="${not empty discountedProducts}">
            <div class="col-md-12">
                <h3>Discounted Expiring Products (New Price)</h3>
                <table class="table table-success table-striped">
                    <thead><tr><th>ID</th><th>Name</th><th>Category</th><th>New Price</th><th>Expiry Date</th></tr></thead>
                    <tbody>
                    <c:forEach var="p" items="${discountedProducts}">
                        <tr>
                            <td><c:out value="${p.id}"/></td>
                            <td><c:out value="${p.name}"/></td>
                            <td><c:out value="${p.category}"/></td>
                            <td><c:out value="${p.price}"/></td>
                            <td><c:out value="${p.expiryDate}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <c:if test="${not empty categoryTotals}">
            <div class="col-md-12">
                <h3>Total Price by Category</h3>
                <table class="table table-bordered">
                     <thead><tr><th>Category</th><th>Total Price</th></tr></thead>
                     <tbody>
                     <c:forEach var="cat" items="${categoryTotals}">
                         <tr>
                             <td><c:out value="${cat.category}"/></td>
                             <td><c:out value="${cat.total_price}"/></td>
                         </tr>
                     </c:forEach>
                     </tbody>
                </table>
            </div>
        </c:if>
    </div>
</div>
</body>
</html>