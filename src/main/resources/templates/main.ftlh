<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Расчет плана питания</title>
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f7f9fc;
            margin: 0;
            padding: 40px 20px;
            display: flex;
            justify-content: center;
        }

        .container {
            background: white;
            max-width: 600px;
            width: 100%;
            padding: 30px 40px;
            border-radius: 12px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
        }

        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
            font-weight: 700;
            letter-spacing: 1px;
        }

        form {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        label {
            font-weight: 600;
            margin-bottom: 6px;
            display: block;
            color: #555;
        }

        input[type="number"],
        select,
        input[type="text"] {
            width: 100%;
            padding: 10px 14px;
            font-size: 16px;
            border: 1.8px solid #ccc;
            border-radius: 8px;
            transition: border-color 0.3s ease;
            background-color: #fafafa;
        }

        input[type="number"]:focus,
        select:focus,
        input[type="text"]:focus {
            outline: none;
            border-color: #4a90e2;
            background-color: #fff;
            box-shadow: 0 0 6px rgba(74, 144, 226, 0.4);
        }

        .checkbox-container {
            max-height: 200px;
            overflow-y: auto;
            border: 1.8px solid #ccc;
            border-radius: 8px;
            padding: 12px 15px;
            background-color: #fafafa;
        }

        .checkbox-container div.product-item {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
            cursor: pointer;
            transition: background-color 0.2s ease;
            border-radius: 6px;
            padding: 4px 8px;
        }

        .checkbox-container div.product-item:hover {
            background-color: #e6f0ff;
        }

        input[type="checkbox"] {
            margin-right: 12px;
            width: 18px;
            height: 18px;
            cursor: pointer;
            accent-color: #4a90e2;
            flex-shrink: 0;
        }

        label[for^="product_"] {
            cursor: pointer;
            user-select: none;
            color: #333;
            font-size: 15px;
        }

        button[type="submit"] {
            background: #4a90e2;
            color: white;
            font-size: 18px;
            font-weight: 700;
            padding: 14px 0;
            border: none;
            border-radius: 10px;
            cursor: pointer;
            box-shadow: 0 4px 12px rgba(74, 144, 226, 0.6);
            transition: background-color 0.3s ease, box-shadow 0.3s ease;
        }

        button[type="submit"]:hover {
            background: #357abd;
            box-shadow: 0 6px 16px rgba(53, 122, 189, 0.8);
        }

        @media (max-width: 480px) {
            .container {
                padding: 20px 25px;
            }

            input[type="number"],
            select,
            input[type="text"] {
                font-size: 14px;
                padding: 8px 10px;
            }

            button[type="submit"] {
                font-size: 16px;
                padding: 12px 0;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Расчет плана питания</h1>

    <form action="/result" method="post">
        <div>
            <label for="userAge">Возраст:</label>
            <input type="number" id="userAge" name="userAge" min="1" max="120" required>
        </div>

        <div>
            <label for="userWeight">Вес (кг):</label>
            <input type="number" id="userWeight" name="userWeight" min="1" step="0.1" required>
        </div>

        <div>
            <label for="userHeight">Рост (см):</label>
            <input type="number" id="userHeight" name="userHeight" min="1" step="0.1" required>
        </div>

        <div>
            <label for="gender">Пол:</label>
            <select id="gender" name="gender" required>
                <option value="">-- Выберите пол --</option>
                <#list genders as gender>
                    <option value="${gender}">${gender}</option>
                </#list>
            </select>
        </div>

        <div>
            <label for="activityType">Уровень активности:</label>
            <select id="activityType" name="activityType" required>
                <option value="">-- Выберите активность --</option>
                <#list UserActivityType as userActivityType>
                    <option value="${userActivityType}">${userActivityType}</option>
                </#list>
            </select>
        </div>

        <div>
            <label for="target">Цель:</label>
            <select id="target" name="target" required>
                <option value="">-- Выберите цель --</option>
                <#list Target as target>
                    <option value="${target}">${target}</option>
                </#list>
            </select>
        </div>

        <div>
            <label for="day">На сколько дней:</label>
            <input type="number" id="day" name="day" min="1" max="120" required>
        </div>

        <div>
            <label>Исключить продукты (отметьте те, которые хотите исключить):</label>
            <input type="text" id="productSearch" placeholder="Введите название продукта...">
        </div>

        <div>
            <div class="checkbox-container" id="productList">
                <#list products as product>
                    <div class="product-item">
                        <input type="checkbox"
                               id="product_${product.id}"
                               name="UncheckedProducts"
                               value="${product.id}">
                        <label for="product_${product

                        .id}">${product.name}</label>
                    </div>
                </#list>
            </div>
        </div>

        <button type="submit">Рассчитать</button>
    </form>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const searchInput = document.getElementById('productSearch');
        const productList = document.getElementById('productList');
        const productItems = productList.querySelectorAll('.product-item');

        searchInput.addEventListener('input', function () {
            const filter = this.value.toLowerCase();

            productItems.forEach(function (item) {
                const label = item.querySelector('label');
                const text = label.textContent.toLowerCase();

                if (text.includes(filter)) {
                    item.style.display = '';
                } else {
                    item.style.display = 'none';
                }
            });
        });
    });
</script>
</body>
</html>

