# CinemaMAK
Aplikacja desktopowa służąca do obsługi multipleksu kinowego

<details>
  <summary>Spis treści</summary>
  <ol>
    <li>
      <a href="#skład">Skład</a>
    </li>
    <li>
      <a href="#technologie">Technologie</a>
    </li>
    <li>
      <a href="#opis-projektu">Opis projektu</a>
    </li>
  </ol>
</details>

## Skład
- Skowron Mateusz
- Chrobot Adrian
- Wilk Karol

## Technologie
- Java
- Gradle
- Spring Framework
- JavaFX
- PostgreSQL

## Opis projektu
Projekt jest to aplikacja desktopowa umożliwiająca obsługę multipleksem kinowym.

Do cześci frontednowej apliakacji została wykorzystana JavaFX, a odpowiednie widoki zaimplementowane w postaci plików FXML.
Część backendowa została zaimplementowana przy użyciu Javy oraz Spring Framework'a.
Dane przechowywane są w relacyjnej bazie danych. Jako system do zarządzania relacyjną bazą danych wybrano jeden z popularniejszych systemów - PostgreSQL.
Automatyzacje procesu kompilacji zapewnia narzędzie - Gradle.

Obecnie aplikacja umożliwa założenie konta w systemie i zalogowanie się, co prezentowane jest na dwóch widokach, pomiędzy którymi można się przełączać.
Użytkownik chcąc założyć konto musi podać niezbędne do tego dane.
Proces uwierzytelniania wymaga podania nazwy użytkownika, którą jest adres email oraz hasła.
Do haszowania haseł wykorzystano funkcję bcrypt. Hasła w postaci zahaszowanej trzymane są w bazie danych w odpowiedniej tabeli.

## Model obiektowy
![](https://drive.google.com/file/d/1517vXnUAKJk1VHqP1eFsv-EVGuGmkdnZ/view?usp=share_link)

## Schemat bazy danych
![](https://drive.google.com/file/d/1O1x8SZu4EULwwaOf_VKA_YBDfxV5Za3K/view?usp=share_link)

## Widoki
- Logowanie
  ![](https://drive.google.com/file/d/1uqqrfrbblOaFH6A33SI2lOPUi5ILJuhV/view?usp=share_link)

- Rejestracja
  ![](https://drive.google.com/file/d/1NXc-9o5183PSvEhxiXLJuzsXcH3Lni53/view?usp=share_link)
