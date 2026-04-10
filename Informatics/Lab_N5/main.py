import pandas as pd

df = pd.read_excel("LAB5.xlsx")

cols = []
for i in range(1, 23):
    if i == 6 or i == 4:
        continue
    cols.append('СТ' + str(i))

res = df.loc[3:14, cols]
print(res)

nums = []
for c in cols:
    if c in ('СТ1','СТ2','СТ5'):
        continue
    nums.append(c)
styled = (
    res.style
      .format("{:.0f}", subset=nums, na_rep="-")
      .highlight_max(color="lightgreen", subset=cols)
      .highlight_min(color="lightsalmon", subset=cols)
    .set_table_styles([{
            "selector": "th, td",
            "props": "border: 1px solid black; padding: 3px;"
    }])
)
html = styled.to_html()
with open("table.html", "w", encoding="utf-8") as f:
    f.write(html)


