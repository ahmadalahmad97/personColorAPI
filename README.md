# PersonColorAPI

## Übersicht
`PersonColorAPI` ist eine Spring Boot-Anwendung, die es ermöglicht, Informationen zu Personen aus einer CSV-Datei zu importieren und diese in einer PostgreSQL-Datenbank zu speichern. Personen sind mit einer Farbe verknüpft, die basierend auf ihrer `personId` zugewiesen wird. Diese Anwendung stellt Endpunkte zur Verfügung, um die importierten Personen zu verwalten und abzufragen, sowie neue Personen in die Datenbank hinzuzufügen.

## Technologien
- **Spring Boot** – Framework zur Erstellung von Webanwendungen
- **PostgreSQL** – Relationale Datenbank zur Speicherung der `Person`-Daten
- **Lombok** – Zur Reduzierung von Boilerplate-Code (z.B. Getter, Setter, Konstruktoren)
- **JUnit** und **Mockito** – Für Unit-Tests und Mocking
- **Gradle** – Build-Tool zur Verwaltung von Abhängigkeiten und Builds

## Setup und Installation

### 1. Voraussetzungen
Stelle sicher, dass du Folgendes installiert hast:
- **Java 23** (für die Entwicklung)
- **Gradle** (für das Build-System)
- **PostgreSQL** (oder ein vergleichbares relationales Datenbanksystem)

### 2. Spring Anwendungskonfiguration

- **spring.application.name**: Der Name der Anwendung.
    - **Beispiel**: `personColorAPI`

### PostgreSQL-Datenbankkonfiguration (mit Umgebungsvariablen)

- **spring.datasource.url**: Die URL der Datenbank.
    - **Beispiel**: `jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:postgres}`
    - **Erklärung**: Diese URL verwendet Umgebungsvariablen für den Host (`DB_HOST`), den Port (`DB_PORT`) und den Datenbanknamen (`DB_NAME`). Standardwerte sind `localhost`, `5432` und `postgres`, falls keine Umgebungsvariablen gesetzt sind.

- **spring.datasource.username**: Der Benutzername für die Datenbankverbindung.
    - **Erklärung**: Wird aus der Umgebungsvariablen `DB_USERNAME` gezogen.

- **spring.datasource.password**: Das Passwort für die Datenbankverbindung.
    - **Erklärung**: Wird aus der Umgebungsvariablen `DB_PASSWORD` gezogen.

### JPA/Hibernate Konfiguration

- **spring.jpa.database-platform**: Der Dialekt für die verwendete Datenbank.
    - **Beispiel**: `org.hibernate.dialect.PostgreSQLDialect`

- **spring.jpa.properties.hibernate.connection.url**: Die URL für die Verbindung zur Datenbank.
    - **Beispiel**: `jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:postgres}`

- **spring.jpa.properties.hibernate.connection.username**: Der Benutzername für die Hibernate-Datenbankverbindung.
    - **Erklärung**: Wird aus der Umgebungsvariablen `DB_USERNAME` gezogen.

- **spring.jpa.properties.hibernate.connection.password**: Das Passwort für die Hibernate-Datenbankverbindung.
    - **Erklärung**: Wird aus der Umgebungsvariablen `DB_PASSWORD` gezogen.

- **spring.jpa.hibernate.ddl-auto**: Gibt an, wie Hibernate das Datenbankschema verwaltet.
    - **Beispiel**: `update`

- **spring.jpa.properties.hibernate.format_sql**: Aktiviert die Formatierung von SQL-Abfragen.
    - **Beispiel**: `true`

- **spring.jpa.properties.hibernate.use_sql_comments**: Aktiviert SQL-Kommentare in den generierten SQL-Abfragen.
    - **Beispiel**: `true`

- **spring.jpa.show-sql**: Zeigt generierte SQL-Abfragen an.
    - **Beispiel**: `false`

### Datei-Konfiguration

- **file.filepath**: Der Pfad für die Speicherung von Dateien.
    - **Beispiel**: `${FILE_PATH}`

### Logging-Konfiguration

- **logging.level.com.example**: Das Logging-Niveau für die Anwendung.
    - **Beispiel**: `DEBUG`

- **logging.file.name**: Der Name der Logdatei.
    - **Beispiel**: `logs/app.log`

### Farben-Konfiguration

Die verfügbaren Farben werden als Liste von Umgebungsvariablen gespeichert:

- **colors.colors.1**: `blau`
- **colors.colors.2**: `grün`
- **colors.colors.3**: `violett`
- **colors.colors.4**: `rot`
- **colors.colors.5**: `gelb`
- **colors.colors.6**: `türkis`
- **colors.colors.7**: `weiß`

### Umgebungsvariablen

Die folgenden Umgebungsvariablen müssen gesetzt werden:

| Umgebungsvariable   | Beschreibung                                      | Standardwert    |
|---------------------|---------------------------------------------------|-----------------|
| `DB_HOST`           | Der Hostname der PostgreSQL-Datenbank             | `localhost`     |
| `DB_PORT`           | Der Port der PostgreSQL-Datenbank                 | `5432`          |
| `DB_NAME`           | Der Name der PostgreSQL-Datenbank                 | `postgres`      |
| `DB_USERNAME`       | Der Benutzername für die Datenbankverbindung      | Keine Standardwert |
| `DB_PASSWORD`       | Das Passwort für die Datenbankverbindung          | Keine Standardwert |
| `FILE_PATH`         | Der Pfad, in dem Dateien gespeichert werden       | Keine Standardwert |
    
### 3. Abhängigkeiten installieren
Das Projekt verwendet **Gradle** zur Verwaltung von Abhängigkeiten. Um alle benötigten Abhängigkeiten zu installieren, führe folgenden Befehl aus:

```bash 
gradle build 
```

### 4. Anwendung starten
Um die Anwendung zu starten, führe den folgenden Befehl aus:

```bash
gradle bootRun
```

## Endpunkte

### 1. GET /persons
**Beschreibung:** Gibt eine Liste aller Personen zurück.

**Antwort:**

- **Erfolgreich (200 OK):**

```json
[{
  "id": 1,
  "name": "Hans",
  "lastname": "Müller",
  "zipcode": "67742",
  "city": "Lauterecken",
  "color": "blau"
}, {
  "id": 2,
  "name": "Maria",
  "lastname": "Schmidt",
  "zipcode": "12345",
  "city": "Berlin",
  "color": "grün"
}]
```

- **Keine Inhalte (204 No Content):** Es wird zurückgegeben, wenn keine Personen vorhanden sind.

### 2. GET /persons/{person_id}

### Beschreibung:
Gibt die Details einer Person basierend auf der person_id zurück. Hierbei kann die Zeilennummer als id verwendet werden.

### Parameter:
- **personId:** Die ID der Person, die abgerufen werden soll.

### Antwort:

- **Erfolgreich (200 OK):** Eine Liste mit der Person, die der angegebenen ID entspricht, wird zurückgegeben.

```json
{
  "id": 1,
  "name": "Hans",
  "lastname": "Müller",
  "zipcode": "67742",
  "city": "Lauterecken",
  "color": "blau"
}
```

- **Nicht gefunden (404 Not Found):** Es wird zurückgegeben, wenn keine Person mit der angegebenen ID gefunden wird.

### 3. GET /persons/color/{color}

### Beschreibung:
Gibt eine Liste aller Personen zurück, die mit der angegebenen Farbe verknüpft sind.

### Parameter:
- **color:** Die Farbe, nach der gesucht wird.

### Antwort:

- **Erfolgreich (200 OK):** Eine Liste der Personen, die der angegebenen Farbe entsprechen, wird zurückgegeben.

```json
[{
    "id": 1,
    "name": "Hans",
    "lastname": "Müller",
    "zipcode": "67742",
    "city": "Lauterecken",
    "color": "blau"
}, {
    "id": 2,
    "name": "Maria",
    "lastname": "Schmidt",
    "zipcode": "12345",
    "city": "Berlin",
    "color": "grün"
}]
```

- **Keine Inhalte (204 No Content):** Wird zurückgegeben, wenn keine Personen mit der angegebenen Farbe gefunden werden.

## 4. POST /persons

### Beschreibung:
Erstellt eine neue Person und fügt diese der Datenbank hinzu.

### Anfrage-Body:
Der Body der Anfrage muss die Informationen der zu erstellenden Person im JSON-Format enthalten.

```json
{
  "name": "John",
  "lastname": "Doe",
  "zipcode": "12345",
  "city": "Berlin",
  "color": "rot"
}
```

### Antwort:

- **Erfolgreich (201 Created):** Eine Antwort mit der neu erstellten Person wird zurückgegeben.

```json
{
    "id": 1,
    "name": "Hans",
    "lastname": "Müller",
    "zipcode": "67742",
    "city": "Lauterecken",
    "color": "blau"
}
```