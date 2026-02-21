<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Калькулятор КБЖУ</title>
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
            max-width: 800px;
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

        #productsForm {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        .product {
            display: grid;
            grid-template-columns: 2fr 1fr 1fr 1fr 1fr;
            gap: 15px;
            padding: 15px;
            border: 1.8px solid #ccc;
            border-radius: 8px;
            background-color: #fafafa;
        }

        .product > div {
            display: flex;
            flex-direction: column;
        }

        label {
            font-weight: 600;
            margin-bottom: 6px;
            display: block;
            color: #555;
            font-size: 14px;
        }

        input[type="text"],
        input[type="number"] {
            width: 100%;
            padding: 10px 14px;
            font-size: 16px;
            border: 1.8px solid #ccc;
            border-radius: 8px;
            transition: border-color 0.3s ease;
            background-color: #fff;
        }

        input[type="text"]:focus,
        input[type="number"]:focus {
            outline: none;
            border-color: #4a90e2;
            box-shadow: 0 0 6px rgba(74, 144, 226, 0.4);
        }

        button {
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

        button:hover {
            background: #357abd;
            box-shadow: 0 6px 16px rgba(53, 122, 189, 0.8);
        }

        .totals {
            margin-top: 30px;
            padding: 20px;
            background-color: #f0f7ff;
            border-radius: 8px;
            border: 1.8px solid #d0e3ff;
        }

        .totals-title {
            font-weight: 700;
            color: #333;
            margin-bottom: 15px;
            font-size: 18px;
        }

        .total-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 8px;
            font-size: 16px;
        }

        .total-value {
            font-weight: 600;
            color: #2c5e9c;
        }

        @media (max-width: 768px) {
            .product {
                grid-template-columns: 1fr;
                gap: 10px;
            }

            .container {
                padding: 20px 25px;
            }

            input[type="text"],
            input[type="number"] {
                font-size: 14px;
                padding: 8px 10px;
            }

            button {
                font-size: 16px;
                padding: 12px 0;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Калькулятор КБЖУ</h1>

    <form id="productsForm" action="#" method="post">
        <div id="productsContainer">
            <div class="product">
                <div>
                    <label>Название продукта</label>
                    <input type="text" name="productName" placeholder="Название" />
                </div>
                <div>
                    <label>Калории (ккал)</label>
                    <input type="number" name="calories" min="0" value="0" />
                </div>
                <div>
                    <label>Белки (г)</label>
                    <input type="number" name="proteins" min="0" value="0" step="0.1" />
                </div>
                <div>
                    <label>Жиры (г)</label>
                    <input type="number" name="fats" min="0" value="0" step="0.1" />
                </div>
                <div>
                    <label>Углеводы (г)</label>
                    <input type="number" name="carbs" min="0" value="0" step="0.1" />
                </div>
            </div>
        </div>

        <button type="button" id="addProductBtn">Добавить продукт</button>
    </form>

    <div class="totals">
        <div class="totals-title">Итог:</div>
        <div class="total-item">
            <span>Калории:</span>
            <span class="total-value" id="totalCalories">0</span> ккал
        </div>
        <div class="total-item">
            <span>Белки:</span>
            <span class="total-value" id="totalProteins">0</span> г
        </div>
        <div class="total-item">
            <span>Жиры:</span>
            <span class="total-value" id="totalFats">0</span> г
        </div>
        <div class="total-item">
            <span>Углеводы:</span>
            <span class="total-value" id="totalCarbs">0</span> г
        </div>
    </div>
</div>

<script>

    const productsContainer = document.getElementById('productsContainer');
    const addProductBtn = document.getElementById('addProductBtn');

    function createProductBlock() {
        const productDiv = document.createElement('div');
        productDiv.classList.add('product');
        productDiv.innerHTML = `
            <div>
                <label>Название продукта</label>
                <input type="text" name="productName" placeholder="Название" />
            </div>
            <div>
                <label>Калории (ккал)</label>
                <input type="number" name="calories" min="0" value="0" />
            </div>
            <div>
                <label>Белки (г)</label>
                <input type="number" name="proteins" min="0" value="0" step="0.1" />
            </div>
            <div>
                <label>Жиры (г)</label>
                <input type="number" name="fats" min="0" value="0" step="0.1" />
            </div>
            <div>
                <label>Углеводы (г)</label>
                <input type="number" name="carbs" min="0" value="0" step="0.1" />
            </div>
        `;
        return productDiv;
    }

    function recalculateTotals() {
        let totalCalories = 0;
        let totalProteins = 0;
        let totalFats = 0;
        let totalCarbs = 0;

        const products = productsContainer.querySelectorAll('.product');

        products.forEach(product => {
            const calories = parseFloat(product.querySelector('input[name="calories"]').value) || 0;
            const proteins = parseFloat(product.querySelector('input[name="proteins"]').value) || 0;
            const fats = parseFloat(product.querySelector('input[name="fats"]').value) || 0;
            const carbs = parseFloat(product.querySelector('input[name="carbs"]').value) || 0;

            totalCalories += calories;
            totalProteins += proteins;
            totalFats += fats;
            totalCarbs += carbs;
        });

        document.getElementById('totalCalories').textContent = totalCalories.toFixed(0);
        document.getElementById('totalProteins').textContent = totalProteins.toFixed(1);
        document.getElementById('totalFats').textContent = totalFats.toFixed(1);
        document.getElementById('totalCarbs').textContent = totalCarbs.toFixed(1);
    }

    function attachInputListeners(productBlock) {
        const inputs = productBlock.querySelectorAll('input[name="calories"], input[name="proteins"], input[name="fats"], input[name="carbs"]');
        inputs.forEach(input => {
            input.addEventListener('input', recalculateTotals);
        });
    }

    const firstProduct = productsContainer.querySelector('.product');
    attachInputListeners(firstProduct);

    addProductBtn.addEventListener('click', () => {
        const newProduct = createProductBlock();
        productsContainer.appendChild(newProduct);
        attachInputListeners(newProduct);
    });

    recalculateTotals();
</script>
</body>
</html>