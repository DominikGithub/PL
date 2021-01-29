# PL  
Propositional logic compiler

## Start
Run `$ javac /src/top/Ex3.java` to evaluate the sentences in `/src/input.txt`


### Example statements
```
False |= True
True |= False

(A /\ B) |= (A <==> B)
(A <==> B) |= A \/ B
(A <==> B) |= ~A \/ B
(A <==> B) |= (A \/ B)
(A <==> B) |= (~A \/ B)

Smoke ==> Smoke
(Smoke ==> Fire) ==> (~Smoke ==> ~Fire)
Smoke \/ Fire \/ ~Fire
(Fire ==> Smoke) /\ Fire /\ ~Smoke

(a <==> b) /\ (b <==> (a <==> ~a)) /\ (c <==> ~b) |= a
(a <==> b) /\ (b <==> (a <==> ~a)) /\ (c <==> ~b) |= ~a
(a <==> b) /\ (b <==> (a <==> ~a)) /\ (c <==> ~b) |= b
(a <==> b) /\ (b <==> (a <==> ~a)) /\ (c <==> ~b) |= ~b
(a <==> b) /\ (b <==> (a <==> ~a)) /\ (c <==> ~b) |= c
(a <==> b) /\ (b <==> (a <==> ~a)) /\ (c <==> ~b) |= ~c
(b <==> (a <==> ~a)) /\ (c <==> ~b) |= a
(b <==> (a <==> ~a)) /\ (c <==> ~b) |= ~a
(b <==> (a <==> ~a)) /\ (c <==> ~b) |= b
(b <==> (a <==> ~a)) /\ (c <==> ~b) |= ~b
(b <==> (a <==> ~a)) /\ (c <==> ~b) |= c
(b <==> (a <==> ~a)) /\ (c <==> ~b) |= ~c

E ==> (~I /\ ~M)
(A /\ W ==> P) /\ (~A ==> I) /\ (~W ==> M) /\ (~P) /\ (E ==> (~I /\ ~M)) /\ (E)
```
