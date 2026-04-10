SELECT COUNT(s.ЧЛВК_ИД) 
FROM Н_УЧЕНИКИ AS s 
JOIN Н_ПЛАНЫ AS plan ON s.ПЛАН_ИД = plan.ИД
JOIN Н_ФОРМЫ_ОБУЧЕНИЯ AS fo ON plan.ФО_ИД = fo.ИД
JOIN Н_ОБУЧЕНИЯ AS ob ON s.ЧЛВК_ИД = ob.ЧЛВК_ИД
JOIN Н_ЛЮДИ AS p ON ob.ЧЛВК_ИД = p.ИД
WHERE fo.ИМЯ_В_ИМИН_ПАДЕЖЕ = 'вечерняя'
AND p.ДАТА_РОЖДЕНИЯ < '2021-04-01';
