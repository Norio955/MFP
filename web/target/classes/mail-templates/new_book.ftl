<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Нова книга</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }
        .container { background-color: #ffffff; max-width: 600px; margin: 0 auto; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        .header { text-align: center; border-bottom: 2px solid #eeeeee; padding-bottom: 20px; margin-bottom: 20px; }
        .content { line-height: 1.6; color: #333333; font-size: 16px; }
        .details-panel { background-color: #f9f9f9; border-left: 4px solid #007bff; padding: 15px; margin: 20px 0; border-radius: 4px; }
        .rare-badge { display: inline-block; background-color: #ffe4b5; color: #a36b00; padding: 5px 10px; border-radius: 4px; font-weight: bold; margin-top: 10px; }
        .footer { text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #eeeeee; font-size: 12px; color: #777777; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <!-- Логотип через raw-URL згідно завдання -->
        <img src="https://raw.githubusercontent.com/VladPiatachenko/MPF_Labs/lab7-freemaker/web/src/main/resources/static/img/logo.png" style="width:150px;" alt="BookApp Logo">
        <h2 style="color: #2c3e50; margin-top: 15px;">Додано нову книгу!</h2>
    </div>

    <div class="content">
        <p>Вітаємо! У нашому каталозі з'явилася нова чудова книга.</p>

        <div class="details-panel">
            <p><strong>Назва:</strong> ${title}</p>
            <p><strong>Автор:</strong> ${author}</p>
            <p><strong>Рік видання:</strong> ${year?c}</p> <!-- ?c прибирає розділювачі тисяч, щоб 2026 не виглядало як 2 026 -->
        </div>

        <!-- Умовний блок для раритетного видання (старіше 2000 року) -->
        <#if year < 2000>
            <p class="rare-badge">⭐ Раритетне видання!</p>
        </#if>

        <!-- Форматування дати -->
        <p style="font-size: 14px; color: #666; margin-top: 20px;">
            <em>Додано до системи: ${added?string('yyyy-MM-dd HH:mm')}</em>
        </p>
    </div>

    <div class="footer">
        <p>BookApp © 2026. Усі права захищено.</p>
        <p>Ви отримали цей лист, оскільки підписані на оновлення каталогу.</p>
    </div>
</div>
</body>
</html>