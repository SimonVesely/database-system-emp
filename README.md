# Databázový systém zaměstnanců

## Autoři
- Simon Veselý — 271017  
- Jan Ručka — 260399

---

## Popis projektu

Projekt představuje konzolovou Java aplikaci pro správu zaměstnanců technologické firmy.

Každý zaměstnanec obsahuje základní identifikační údaje a evidenci spoluprací s ostatními zaměstnanci včetně kvality vztahu.

Součástí systému je také lokální SQL databáze SQLite, která slouží jako záloha dat mezi jednotlivými spuštěními programu.

Aplikace využívá principy objektově orientovaného programování, dědičnost, abstraktní třídy, rozhraní a dynamické datové struktury.

---

## Datový model zaměstnance

Každý zaměstnanec obsahuje:

- automaticky generované ID
- jméno
- příjmení
- rok narození
- skupinu zaměstnance
- seznam spolupracovníků
- úroveň spolupráce:
  - BAD
  - AVERAGE
  - GOOD

---

## Typy zaměstnanců

### Datoví analytici

Disponují analytickou dovedností:

- nalezení zaměstnance s největším počtem společných spolupracovníků

### Bezpečnostní specialisté

Disponují bezpečnostní dovedností:

- výpočet rizikového skóre zaměstnance
- hodnocení spolupráce pomocí vlastního algoritmu založeného na kvalitě vztahů a počtu vazeb

> Typ zaměstnance je zvolen při vytvoření a nelze jej později změnit.

---

## Implementované funkce

- přidání zaměstnance
- odebrání zaměstnance
- vyhledání zaměstnance podle ID nebo jména
- přidání spolupráce mezi zaměstnanci
- odstranění vazeb při smazání zaměstnance
- spuštění speciální dovednosti zaměstnance
- abecední výpis zaměstnanců podle příjmení
- statistiky systému
- počet zaměstnanců v jednotlivých skupinách
- automatické načtení dat z SQLite při spuštění
- automatické uložení dat do SQLite při ukončení

---

## Statistiky systému

Program umí zobrazit:

- zaměstnance s nejvyšším počtem spoluprací
- převažující kvalitu spolupráce v systému
- počet zaměstnanců dle skupin

---

## Použité technologie

- Java
- SQLite
- JDBC Driver (`sqlite-jdbc`)
- OOP návrh
- `ArrayList`
- `HashMap`

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

## Spuštění projektu manuálně(Linux/MacOS)

```bash
java -cp "bin:lib/sqlite-jdbc-3.53.0.0.jar" database_system_emp.Main
```

## Licence
Tento projekt je licencován pod MIT licencí.

Viz soubor: [LICENSE](LICENSE)
