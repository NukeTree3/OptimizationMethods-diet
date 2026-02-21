<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Главная страница</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #4CAF50;
            color: white;
        }
    </style>
</head>
<body>
<h1>Все продукты в базе</h1>
<table>
    <thead>
    <tr>
        <th>Название</th>
        <th>Калории</th>
        <th>Белки</th>
        <th>Жиры</th>
        <th>Цена</th>
    </tr>
    </thead>
    <tbody>
    <#list products as product>
        <tr>
            <td>${product.name}</td>
            <td>${product.calories}</td>
            <td>${product.proteins}</td>
            <td>${product.fats}</td>
            <td>${product.price}</td>
        </tr>
    </#list>
    </tbody>
</table>
</body>
</html>