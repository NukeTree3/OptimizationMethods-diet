<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8" />
    <title>Главная страница</title>
    <style>
        body {
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f4f8;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            color: #2c3e50;
            flex-direction: column;
        }

        h1 {
            margin-bottom: 40px;
            font-weight: 700;
            font-size: 2.5rem;
            color: #2980b9;
            text-align: center;
        }

        nav {
            display: flex;
            gap: 30px;
            flex-wrap: wrap;
            justify-content: center;
        }

        nav a {
            text-decoration: none;
            background-color: #2980b9;
            color: white;
            padding: 14px 28px;
            border-radius: 8px;
            font-size: 1.2rem;
            font-weight: 600;
            box-shadow: 0 4px 10px rgba(41, 128, 185, 0.3);
            transition: background-color 0.3s ease, box-shadow 0.3s ease;
        }

        nav a:hover, nav a:focus {
            background-color: #1c5980;
            box-shadow: 0 6px 14px rgba(28, 89, 128, 0.6);
            outline: none;
        }

        @media (max-width: 480px) {
            nav {
                flex-direction: column;
                gap: 20px;
            }

            nav a {
                width: 100%;
                text-align: center;
            }
        }
    </style>
</head>
<body>
<h1>Главная страница</h1>
<nav>
    <a href="/go">Расчет диеты</a>
    <a href="/self">Калькулятор</a>
    <a href="/all-prouducts">Все продукты</a>
</nav>
</body>
</html>

