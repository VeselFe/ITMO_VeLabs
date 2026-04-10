-- Запрос №1
SELECT Н_ОЦЕНКИ.ПРИМЕЧАНИЕ, Н_ВЕДОМОСТИ.ИД FROM Н_ВЕДОМОСТИ  
RIGHT JOIN Н_ОЦЕНКИ ON Н_ВЕДОМОСТИ.ОЦЕНКА = Н_ОЦЕНКИ.КОД 
WHERE Н_ОЦЕНКИ.ПРИМЕЧАНИЕ = 'освобождение' 
AND Н_ВЕДОМОСТИ.ДАТА = '2022-06-08';

-- Запрос №2
SELECT Н_ВЕДОМОСТИ.ЧЛВК_ИД, Н_ЛЮДИ.ИМЯ, Н_ВЕДОМОСТИ.ЧЛВК_ИД, Н_СЕССИЯ.УЧГОД 
FROM Н_ЛЮДИ 
LEFT JOIN Н_ВЕДОМОСТИ ON Н_ЛЮДИ.ИД = Н_ВЕДОМОСТИ.ЧЛВК_ИД
LEFT JOIN Н_СЕССИЯ ON Н_ЛЮДИ.ИД = Н_СЕССИЯ.ЧЛВК_ИД
WHERE Н_ЛЮДИ.ИМЯ > 'Николай'
AND Н_СЕССИЯ.ДАТА > '2002-01-04'
AND Н_ВЕДОМОСТИ.ЧЛВК_ИД = 153285;

-- Запрос №3
SELECT COUNT(s.ЧЛВК_ИД) 
FROM Н_УЧЕНИКИ AS s 
JOIN Н_ПЛАНЫ AS plan ON s.ПЛАН_ИД = plan.ИД
JOIN Н_ФОРМЫ_ОБУЧЕНИЯ AS fo ON plan.ФО_ИД = fo.ИД
JOIN Н_ОБУЧЕНИЯ AS ob ON s.ЧЛВК_ИД = ob.ЧЛВК_ИД
JOIN Н_ЛЮДИ AS p ON ob.ЧЛВК_ИД = p.ИД
WHERE fo.ИМЯ_В_ИМИН_ПАДЕЖЕ = 'вечерняя'
AND p.ДАТА_РОЖДЕНИЯ < '2021-04-01';


-- Запрос №4
SELECT s.ГРУППА
FROM Н_УЧЕНИКИ AS s
JOIN Н_ПЛАНЫ AS plan ON s.ПЛАН_ИД = plan.ИД
JOIN Н_ОТДЕЛЫ AS otd ON plan.ОТД_ИД = otd.ИД
WHERE otd.КОРОТКОЕ_ИМЯ = 'КТиУ' -- ФКТИУ нет в списке отделов поэтому ищем КТиУ
AND plan.УЧЕБНЫЙ_ГОД = '2011/2012'
AND s.СОСТОЯНИЕ = 'утвержден'
GROUP BY s.ГРУППА
HAVING COUNT(s.ЧЛВК_ИД) < 10;


-- Запрос №5
SELECT 
	s.ИД AS "Номер", 
	p.ФАМИЛИЯ, 
	p.ИМЯ, 
	p.ОТЧЕСТВО, 
	ROUND(AVG(ved.ОЦЕНКА::numeric), 2) AS "СР_оценка"
FROM Н_УЧЕНИКИ AS s
JOIN Н_ЛЮДИ AS p ON s.ЧЛВК_ИД = p.ИД
JOIN Н_ВЕДОМОСТИ AS ved ON p.ИД = ved.ЧЛВК_ИД
WHERE s.ГРУППА = '4100' 
AND ved.ОЦЕНКА IN ('2', '3', '4', '5')
GROUP BY
s.ИД, p.ФАМИЛИЯ, p.ИМЯ, p.ОТЧЕСТВО
HAVING ROUND(AVG(ved.ОЦЕНКА::numeric)) = (
SELECT MAX(v.ОЦЕНКА::numeric) 
FROM Н_ВЕДОМОСТИ AS v
JOIN Н_ЛЮДИ AS l ON v.ЧЛВК_ИД = l.ИД
JOIN Н_УЧЕНИКИ AS st ON l.ИД = st.ЧЛВК_ИД
WHERE st.ГРУППА = '1100'
AND v.ОЦЕНКА IN ('2', '3', '4', '5')
);


-- Запрос №6
SELECT 
s.ГРУППА, 
s.ИД, 
p.ФАМИЛИЯ, 
p.ИМЯ, 
p.ОТЧЕСТВО, 
s.П_ПРКОК_ИД AS "НОМЕР ПРИКАЗА" 
FROM Н_УЧЕНИКИ AS s
JOIN Н_ПЛАНЫ AS plan ON s.ПЛАН_ИД = plan.ИД
JOIN Н_НАПРАВЛЕНИЯ_СПЕЦИАЛ AS spec ON plan.НАПС_ИД = spec.ИД
JOIN Н_НАПР_СПЕЦ AS nsp ON spec.НС_ИД = nsp.ИД 
JOIN Н_ЛЮДИ AS p ON s.ЧЛВК_ИД = p.ИД
JOIN Н_ФОРМЫ_ОБУЧЕНИЯ AS form ON plan.ФО_ИД = form.ИД
WHERE nsp.НАИМЕНОВАНИЕ = 'Программная инженерия'
AND s.КОНЕЦ > '2012-08-01'
AND form.НАИМЕНОВАНИЕ IN ('Очная','Заочная');


-- Запрос №7
SELECT s.ИД, p.ФАМИЛИЯ, p.ИМЯ
FROM Н_УЧЕНИКИ AS s
JOIN Н_ЛЮДИ AS p ON s.ЧЛВК_ИД = p.ИД
WHERE p.ФАМИЛИЯ IN (
SELECT p.ФАМИЛИЯ 
FROM Н_ЛЮДИ AS p
GROUP BY p.ФАМИЛИЯ
HAVING COUNT(*) > 1
)
ORDER BY p.ФАМИЛИЯ;


-- Переделанные запросы(ДОП)
-- №5
SELECT 
	s.ИД AS "Номер", 
	p.ФАМИЛИЯ, 
	p.ИМЯ, 
	p.ОТЧЕСТВО, 
	ROUND(AVG(ved.ОЦЕНКА::numeric), 2) AS "СР_оценка"
FROM Н_УЧЕНИКИ AS s
JOIN Н_ЛЮДИ AS p ON s.ЧЛВК_ИД = p.ИД
JOIN Н_ВЕДОМОСТИ AS ved ON p.ИД = ved.ЧЛВК_ИД
WHERE s.ГРУППА = '4100' 
AND ved.ОЦЕНКА IN ('2', '3', '4', '5')
GROUP BY s.ИД, p.ФАМИЛИЯ, p.ИМЯ, p.ОТЧЕСТВО
HAVING ROUND(AVG(ved.ОЦЕНКА::numeric), 1) = (
	SELECT MAX(mid) FROM (
		SELECT ROUND(AVG(v.ОЦЕНКА::numeric), 1) AS mid 
		FROM Н_ВЕДОМОСТИ AS v
		JOIN Н_ЛЮДИ AS l ON v.ЧЛВК_ИД = l.ИД
		JOIN Н_УЧЕНИКИ AS st ON l.ИД = st.ЧЛВК_ИД
		WHERE st.ГРУППА = '1100'
		AND v.ОЦЕНКА IN ('2', '3', '4', '5')
		GROUP BY st.ИД 
	)
);


-- №7
SELECT s.ИД, p.ФАМИЛИЯ, p.ИМЯ
FROM Н_УЧЕНИКИ AS s
JOIN Н_ЛЮДИ AS p ON s.ЧЛВК_ИД = p.ИД
WHERE p.ИД IN (
	SELECT p.ИД 
	FROM Н_ЛЮДИ as p
	CROSS JOIN Н_ЛЮДИ AS p1  
	WHERE p.ФАМИЛИЯ = p1.ФАМИЛИЯ 
	AND p.ИД <> p1.ИД
)
ORDER BY p.ФАМИЛИЯ;
