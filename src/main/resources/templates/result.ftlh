<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Оптимальный план питания</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }

        h1 {
            text-align: center;
            color: #333;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

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

        .summary {
            margin-top: 20px;
            font-weight: bold;
            color: #333;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Оптимальный план питания</h1>
    <table>
        <thead>
        <tr>
            <th>Название</th>
            <th>Количество</th>
            <th>Калории</th>
            <th>Белки</th>
            <th>Жиры</th>
            <th>Углеводы</th>
            <th>Цена</th>
        </tr>
        </thead>
        <tbody>
<#--        <#list result.productsWithCountAndPrices as product>-->
<#--            <tr>-->
<#--                <td>${product[0]}</td>-->
<#--                <td>${product[1]}</td>-->
<#--                <td>${product[2]}</td>-->
<#--            </tr>-->
<#--        </#list>-->
        <#list dietPlan as dietOnDay>
            <tr>
                <td colspan="7">День ${dietOnDay_index+1}</td>
            </tr>
            <#list dietOnDay as dietOnTime>
                <tr>
                    <td colspan="7">Прием пищи ${dietOnTime_index+1}</td>
                </tr>
                <#list dietOnTime.products as product>
                    <tr>
                        <td>${product.name}</td>
                        <td>${product.count}</td>
                        <td>${product.calories}</td>
                        <td>${product.proteins}</td>
                        <td>${product.fats}</td>
                        <td>${product.carbohydrates}</td>
                        <td>${product.price}</td>
                    </tr>
                </#list>
            </#list>
        </#list>
        </tbody>
    </table>
    <div class="summary">
        Общая стоимость: ${result.totalCost} <br>
        Калории: ${result.totalCalories} <br>
        Белки: ${result.totalProteins} <br>
        Жиры: ${result.totalFats} <br>
        Углеводы: ${result.totalCarbohydrates}
    </div>
</div>
</body>
</html>


