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
