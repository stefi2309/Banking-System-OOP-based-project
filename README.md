# IVANA STEFANIA 332CD
# Project Assignment POO  - J. POO Morgan - Phase One

## Implementarea Sistemului Bancar
Acest proiect Java implementeaza un sistem bancar simplificat unde utilizatorii pot crea conturi, efectua transferuri, seta aliasuri pentru conturi, si multe altele. 
Sistemul gestioneaza interactiunile intre utilizatori, conturi, si tranzactiile efectuate prin intermediul unei serii de clase si metode.

## Componente Cheie

### User si Account
- User: Reprezinta un utilizator in sistem, gestionand detaliile acestuia si conturile asociate.
- Account: Gestioneaza detalii ale contului cum ar fi balanta, moneda si tranzactiile asociate.

### Bank
- Bank: Clasa principala care gestioneaza toti utilizatorii si interactiunile intre conturile acestora.

### Card si Strategii de Tranzactie
- Card: Baza pentru cardurile bancare, gestionand numarul cardului si starea acestuia.
- CardTransactionStrategy: Interfata pentru strategiile de tranzactie, cu implementari specifice pentru diferite tipuri de carduri.

### Mecanici de Sistem
- Singleton Pattern: Utilizat pentru a asigura ca exista o singura instanta a clasei Bank.
- Factory Pattern: Utilizat in CardFactory pentru a crea carduri de diferite tipuri.
- Exchange Rate Management: Gestionarea ratelor de schimb valutar si conversia sumelor intre diferite monede.

## Caracteristici
- Gestionarea Conturilor si Utilizatorilor: Permite crearea si administrarea conturilor si utilizatorilor.
- Tranzactii si Transferuri: Implementeaza logica necesara pentru efectuarea platilor si transferurilor intre conturi, inclusiv conversia valutara.
- Gestionarea Cardurilor: Permite crearea si administrarea cardurilor, oferind suport pentru carduri de unica folosinta si carduri regulate.

## Descrierea Claselor

### User 
- Descriere: Gestioneaza informatiile despre un utilizator si conturile sale.
- Proprietati: Nume, email, lista de conturi.
- Metode: Metode pentru adaugarea de conturi si obtinerea informatiilor despre conturi si carduri.

### Account
- Descriere: Reprezinta un cont bancar, gestionand balanta, moneda, si alte detalii relevante.
- Proprietati: IBAN, balanta, moneda, tipul contului.
- Metode: Metode pentru depuneri, plati si gestionarea cardurilor asociate.

### Bank
- Descriere: Clasa centrala care coordoneaza toate activitatile sistemului bancar.
- Proprietati: Lista de utilizatori.
- Metode: Metode pentru gestionarea utilizatorilor, conturilor si efectuarea de transferuri si alte operatiuni bancare.

### Card
- Descriere: Baza pentru gestionarea cardurilor bancare.
- Proprietati: Numarul cardului, statusul.
- Metode: Metoda pentru efectuarea tranzactiilor conform strategiei asociate cardului.

### CardFactory
- Descriere: Fabrica pentru crearea de carduri, folosind diferite strategii bazate pe tipul cardului.
- Metode: Metoda statica pentru crearea cardurilor.

### Exchange
- Descriere: Gestionarea si conversia valutara intre diverse monede.
- Metode: Metode pentru adaugarea ratelor de schimb si conversia sumelor intre monede.

### TransactionResult
- Descriere: Encapsuleaza rezultatul unei tranzactii efectuate cu un card.
- Proprietati: Succesul tranzactiei, daca cardul trebuie inlocuit.