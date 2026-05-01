# Databázový systém zaměstnanců

## Autoři
- Simon Veselý — 271017
- Jan Ručka — 260399

---

## Popis projektu

Konzolová Java aplikace pro správu zaměstnanců technologické firmy.
Každý zaměstnanec obsahuje základní identifikační údaje a evidenci spoluprací s ostatními zaměstnanci včetně kvality každého vztahu.
Součástí systému je lokální SQLite databáze, která slouží jako záloha dat mezi jednotlivými spuštěními programu.
Aplikace využívá principy objektově orientovaného programování: dědičnost, abstraktní třídy, rozhraní a dynamické datové struktury.

---

## Datový model zaměstnance

Každý zaměstnanec obsahuje:

- automaticky generované ID
- jméno
- příjmení
- rok narození
- skupinu zaměstnance
- seznam spolupracovníků a kvalitu každé spolupráce:
  - `BAD`
  - `AVERAGE`
  - `GOOD`

---

## Typy zaměstnanců

### Datoví analytici

Datoví analytici mohou spustit dovednost **analýzy společných spolupracovníků** (podrobně popsáno níže).

### Bezpečnostní specialisté

Bezpečnostní specialisté mohou spustit dovednost **výpočtu rizikového skóre pomocí Markovova řetězce** (podrobně popsáno níže).

> Typ zaměstnance je zvolen při vytvoření a nelze jej později změnit.

---

## Algoritmy

### Datový analytik — Analýza společných spolupracovníků

**Cíl:** Pro zaměstnance A najít zaměstnance B, který sdílí s A nejvíce společných spolupracovníků.

**Postup:**

1. Sestavíme množinu ID všech spolupracovníků zaměstnance A — označme ji `coopA`.
2. Pro každého dalšího zaměstnance B v systému sestavíme jeho množinu ID spolupracovníků `coopB`.
3. Vypočítáme průnik `coopA ∩ coopB` pomocí `Set.retainAll()`.
4. Velikost průniku je počet společných spolupracovníků B s A.
5. Vrátíme zaměstnance B s nejvyšším počtem.

**Složitost:** O(n · k), kde n je celkový počet zaměstnanců a k je průměrný počet spoluprací na zaměstnance (průnik množin je O(k)).

**Příklad:**

```
A spolupracuje s: {B, C, D}
X spolupracuje s: {B, C, E}   → průnik = {B, C} → skóre 2
Y spolupracuje s: {B, F}      → průnik = {B}    → skóre 1

Výsledek: X je nejlepší shoda pro A (skóre 2).
```

---

### Bezpečnostní specialista — Rizikové skóre pomocí Markovova řetězce

**Cíl:** Vyhodnotit dlouhodobou kvalitu spolupráce zaměstnance jako jediné číslo v rozsahu [1,0 ; 3,0].

**Stavy:** Model pracuje se třemi stavy reprezentujícími kvalitu spolupráce:

| Index stavu | Význam  |
|:-----------:|---------|
| 0           | BAD     |
| 1           | AVERAGE |
| 2           | GOOD    |

**Krok 1 — Vnější faktor**

Projdeme všechny spolupráce, přičteme +1 za každou GOOD a −1 za každou BAD. Vydělíme celkovým počtem spoluprací a získáme normalizovanou hodnotu v rozsahu [−1, +1]:

```
outsideFactor = (počet(GOOD) - počet(BAD)) / celkový počet spoluprací
```

**Krok 2 — Přechodová matice**

Vyjdeme ze základní přechodové matice P_base, která popisuje neutrální přechody mezi stavy:

```
P_base = [ [0.5, 0.4, 0.1],
           [0.2, 0.6, 0.2],
           [0.1, 0.4, 0.5] ]
```

Každý řádek upravíme pomocí `outsideFactor` a pevné citlivosti `sensitivity = 0.4`:

- Je-li `outsideFactor > 0` (více GOOD než BAD): zvýšíme sloupec 2 (GOOD) o `outsideFactor × 0.4`
- Je-li `outsideFactor < 0` (více BAD než GOOD): zvýšíme sloupec 0 (BAD) o `|outsideFactor| × 0.4`

Každý řádek normalizujeme tak, aby součet jeho prvků byl roven 1.

**Krok 3 — Iterace**

Začneme s rozdělením pravděpodobnosti `v = [0.0, 1.0, 0.0]` (stav AVERAGE) a aplikujeme přechodovou matici 20×:

```
v_next[j] = Σ_i  v[i] × P[i][j]
```

Po 20 iteracích se v přiblíží stacionárnímu rozdělení.

**Krok 4 — Výsledné skóre**

Stacionární rozdělení převedeme na skalár:

```
skóre = v[0] × 1.0  +  v[1] × 2.0  +  v[2] × 3.0
```

| Skóre   | Interpretace                             |
|:-------:|------------------------------------------|
| → 1,0   | Vysoké riziko — převažuje BAD            |
| → 2,0   | Neutrální — převažuje AVERAGE            |
| → 3,0   | Nízké riziko — převažuje GOOD            |

---

## Implementované funkce

- přidání zaměstnance
- odebrání zaměstnance (všechny vazby jsou automaticky odstraněny)
- vyhledání zaměstnance podle ID nebo jména / příjmení
- přidání spolupráce mezi zaměstnanci
- spuštění skupinové dovednosti zaměstnance
- abecední výpis zaměstnanců podle příjmení, rozdělený dle skupin
- statistiky systému
- počet zaměstnanců ve skupinách
- uložení jednoho nebo všech zaměstnanců do textového souboru
- načtení zaměstnanců z textového souboru
- automatické načtení dat z SQLite při spuštění programu
- automatické uložení dat do SQLite při ukončení programu

---

## Statistiky systému

Program umí zobrazit:

- zaměstnance s nejvyšším počtem vazeb spolupráce
- převažující kvalitu spolupráce v celém systému
- počet zaměstnanců dle skupin

---

## Použité technologie

| Technologie | Účel |
|---|---|
| Java | Aplikační jazyk |
| SQLite | Perzistentní SQL záloha |
| JDBC (`sqlite-jdbc`) | Ovladač SQLite |
| `LinkedHashMap` | Hlavní úložiště zaměstnanců (ID → zaměstnanec) |
| `HashMap` | Mapa spoluprací každého zaměstnance (ID partnera → kvalita) |
| `HashSet` / `Set.retainAll` | Průnik množin v dovednosti datového analytika |

---

## Struktura projektu

```text
src/
└── database_system_emp/
    ├── Main.java
    ├── ConsoleInterface.java
    ├── Database.java
    ├── Employee.java
    ├── DataAnalyst.java
    ├── SecuritySpecialist.java
    ├── Groups.java
    ├── CooperationQuality.java
    └── Skill.java
```

---

## Spuštění projektu manuálně (Linux / macOS)

```bash
java -cp "bin:lib/sqlite-jdbc-3.53.0.0.jar" database_system_emp.Main
```

---

## Licence

Tento projekt je licencován pod licencí MIT.
Viz soubor: [LICENSE](LICENSE)
