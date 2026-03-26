# Databázový systém zaměstnanců

## Rozdělení práce
### 1. Týden
- Návrh a implementace abstraktní třídy `Zamestnanec`
- Implementace tříd `DatovyAnalytik` a `BezpecnostniSpecialista`
- Interface `IDovednost`
- Metody pro přidání/odebrání spolupráce a zaměstnance
- Základní statistiky jednoho zaměstnance
- Hlavní třída `Firma` (použití `HashMap<Integer, Zamestnanec>`)
- Základní textové menu
- Serializace / ukládání a načítání jednoho zaměstnance do souboru
- Implementace funkcí a), b), c), d)

### 2. Týden
- Implementace specifických dovedností skupin:
  - `nejviceSpolecnychSpolupracovniku()` pro Datového analytika
  - `vypocitejRizikoveSkore()` pro Bezpečnostního specialistu
- Spuštění dovednosti podle skupiny (funkce e)
- Abecední výpis zaměstnanců podle příjmení ve skupinách (f)
- Globální statistiky (g) – převažující kvalita spolupráce + zaměstnanec s nejvíce vazbami
- Výpis počtu zaměstnanců ve skupinách (h)
- Validace vstupů a ošetření chyb
- Vylepšení menu a uživatelského rozhraní

### 2. Týden
- Třída `SqlBackup` – ukládání všech dat do SQLite při ukončení programu (k)
- Načítání dat z SQLite při spuštění (l)
- Dokumentace (JavaDoc) + README
- Testování celého systému (včetně režimu bez SQL)
- Komplexní ošetření výjimek
- Finální ladění, testování edge cases
- Příprava projektu k odevzdání (struktura, kompilace, spuštění)

## Autoři
* Simon Veselý - 271017
* Jan Ručka - 260399

## Popis projektu
Tento projekt představuje jednoduchý databázový systém pro správu zaměstnanců technologické firmy. Každý zaměstnanec má své identifikační údaje a seznam spolupracovníků včetně úrovně spolupráce.

Systém umožňuje evidenci zaměstnanců, správu vztahů mezi nimi a provádění analytických a bezpečnostních operací.

---

## Datový model

Každý zaměstnanec obsahuje:
- ID (automaticky generované)
- jméno
- příjmení
- rok narození
- seznam spolupracovníků:
  - ID spolupracovníka
  - úroveň spolupráce (špatná / průměrná / dobrá)

---

## Skupiny zaměstnanců

### Datoví analytici
- určují, se kterým spolupracovníkem mají nejvíce společných spolupracovníků

### Bezpečnostní specialisté
- vyhodnocují rizikovost spolupráce
- počítají rizikové skóre (vlastní algoritmus)

> Každý zaměstnanec je při vytvoření zařazen do jedné skupiny a nelze jej později změnit.

---

## Funkcionalita

- Přidání zaměstnance
- Přidání spolupráce mezi zaměstnanci
- Odebrání zaměstnance (včetně vazeb)
- Vyhledání zaměstnance podle ID
- Spuštění specifické dovednosti dle skupiny
- Abecední výpis zaměstnanců podle příjmení ve skupinách
- Statistiky:
  - převažující kvalita spolupráce
  - zaměstnanec s nejvíce vazbami
- Výpis počtu zaměstnanců ve skupinách
- Uložení zaměstnance do souboru
- Načtení zaměstnance ze souboru
- Uložení všech dat do SQL databáze při ukončení
- Načtení dat z SQL databáze při spuštění

> SQL databáze slouží pouze jako záloha – aplikace musí fungovat i bez ní.

---

## Technické požadavky

- Objektově orientované programování (OOP)
- Použití alespoň jedné abstraktní třídy nebo rozhraní
- Použití alespoň jedné dynamické datové struktury

---

## Spuštění projektu manuálně

```bash
javac -d bin src/database_system_emp/*.java
java -cp bin database_system_emp.Main
```

## Licence
Tento projekt je licencován pod MIT licencí.

Viz soubor: [LICENSE](LICENSE)
