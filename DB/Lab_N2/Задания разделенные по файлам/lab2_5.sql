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
