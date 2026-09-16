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
