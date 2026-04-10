SELECT s.ГРУППА
FROM Н_УЧЕНИКИ AS s
JOIN Н_ПЛАНЫ AS plan ON s.ПЛАН_ИД = plan.ИД
JOIN Н_ОТДЕЛЫ AS otd ON plan.ОТД_ИД = otd.ИД
WHERE otd.КОРОТКОЕ_ИМЯ = 'КТиУ' -- ФКТИУ нет в списке отделов поэтому ищем КТиУ
AND plan.УЧЕБНЫЙ_ГОД = '2011/2012'
AND s.СОСТОЯНИЕ = 'утвержден'
GROUP BY s.ГРУППА
HAVING COUNT(s.ЧЛВК_ИД) < 10;
