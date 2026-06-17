# Documentation detaillee du projet `hospital`

## 1. Vue d'ensemble

Ce projet Java represente un petit systeme d'hopital avec :

- des **entites metier** : patients, medecins, visiteurs, maladies
- des **objets utilitaires** : chambres, dates, remedes, hopital
- une **interface graphique Swing** pour visualiser l'hopital
- un **point d'entree** qui construit un scenario de test

Le programme ne repose pas sur une base de donnees ni sur des fichiers externes de donnees. Tout est cree directement dans le code, surtout dans `main/Main.java`.

L'architecture generale est la suivante :

- `accessory` : objets de support et logique globale de l'hopital
- `entity` : modeles des personnes et des maladies
- `fenetre` : interface graphique et dessin
- `main` : lancement du programme

---

## 2. Arborescence du projet

```text
hospital/
├─ accessory/
│  ├─ Chamber.java
│  ├─ Date.java
│  ├─ Hospital.java
│  ├─ Remedy.java
│  └─ Taken.java
├─ entity/
│  ├─ Disease.java
│  ├─ Doctor.java
│  ├─ Human.java
│  ├─ Patient.java
│  └─ Visitor.java
├─ fenetre/
│  ├─ ChamberPainter.java
│  ├─ ControlPanel.java
│  ├─ Dessin.java
│  ├─ Ecoute.java
│  ├─ HumanPainter.java
│  ├─ MaFenetre.java
│  └─ PlanPalette.java
├─ main/
│  └─ Main.java
├─ .bin/
│  └─ fichiers compiles `.class`
├─ run.ps1
├─ run.sh
└─ sujet.txt
```

---

## 3. Fonctionnement global

Le cycle principal du programme est le suivant :

1. `main/Main.java` cree des maladies, remedes, patients, medecins, visiteurs et chambres.
2. Tous ces objets sont ranges dans un objet `Hospital`.
3. `MaFenetre` ouvre une fenetre Swing.
4. `Dessin` dessine les chambres et les humains.
5. `Ecoute` reagit a la souris.
6. `ControlPanel` permet de selectionner une personne, la deplacer et, si c'est un medecin, administrer un remede a un patient.
7. `Hospital` applique les regles metier principales, par exemple :
   - verifier si un deplacement reste dans les limites
   - trouver la chambre d'une personne
   - recuperer les patients d'une chambre
   - administrer un remede si les conditions sont respectees

---

## 4. Package `accessory`

Ce package contient les classes d'appui du modele metier.

### `accessory/Chamber.java`

#### Role

Represente une chambre de l'hopital.

#### Attributs

- `id` : numero de la chambre
- `shape` : rectangle geometrique qui represente la zone de la chambre dans le plan

Le rectangle est un `Rectangle2D.Double`, donc chaque chambre est definie par :

- une position `(x, y)`
- une largeur
- une hauteur

#### Constructeur

- `Chamber(int id, Rectangle2D.Double shape)` : initialise le numero et la forme

#### Methodes principales

- `getId()` : retourne l'identifiant
- `getShape()` : retourne le rectangle
- `setId(...)` et `setShape(...)` : modifient les attributs
- `contient(double x, double y)` : verifie si un point se trouve a l'interieur de la chambre

#### Importance dans le projet

Cette methode `contient(...)` est centrale, car elle est utilisee pour :

- savoir dans quelle chambre se trouve une personne
- detecter la chambre sous la souris
- recuperer les patients presents dans une chambre

---

### `accessory/Date.java`

#### Role

Represente une date simple maison, sans utiliser `java.time`.

#### Attributs

- `day`
- `month`
- `year`

#### Constructeur

- `Date(int day, int month, int year)`

Le constructeur effectue quelques verifications de validite :

- mois entre 1 et 12
- jours compatibles avec le mois
- gestion simple de fevrier et des annees bissextiles

Si la date est invalide, le code affiche un message d'erreur dans la console, mais ne lance pas d'exception.

#### Methodes principales

- `display()` : affiche la date dans la console
- `compare(Date date)` :
  - `1` si `this` est plus grande
  - `-1` si `this` est plus petite
  - `0` si les deux dates sont egales
- `bissextile()` : indique si l'annee est consideree comme bissextile
- `add_day(int add_day)` : ajoute ou retire des jours a la date courante
- `age()` : calcule un age par rapport a une date fixe `27/03/2026`
- `toString()` : renvoie la date sous forme de texte

#### Remarques importantes

- La classe est mutable : `add_day(...)` modifie directement l'objet.
- Certaines methodes ont des limites logiques. Par exemple `age()` depend d'une date du jour ecrite en dur.
- `bissextile()` n'applique pas exactement la vraie regle gregorienne.

Cette classe reste suffisante pour le mini-projet, mais elle serait fragile dans une application reelle.

---

### `accessory/Hospital.java`

#### Role

C'est la classe centrale du systeme. Elle contient la liste des personnes et la liste des chambres, puis fournit les operations metier globales.

#### Attributs

- `DEFAULT_WIDTH`, `DEFAULT_HEIGHT` : dimensions minimales du plan
- `personne` : collection de toutes les personnes de l'hopital
- `chambers` : collection de toutes les chambres

Les collections sont des `Vector` non generiques. Cela signifie que le code repose ensuite sur des casts, par exemple `(Chamber)` ou `(Patient)`.

#### Constructeur

- `Hospital(Vector personne, Vector chambers)`

#### Methodes principales

- `getPersonne()` : retourne toutes les personnes
- `getChambers()` : retourne toutes les chambres
- `check()` : simple message de verification manuelle
- `computeBounds()` :
  - calcule les dimensions utiles du plan a partir des chambres
  - sert a limiter les deplacements
- `moveHumanTo(Human human, double x, double y)` :
  - refuse un deplacement hors des limites du plan
  - sinon modifie la position de l'humain
- `findChamberForHuman(Human human)` :
  - parcourt toutes les chambres
  - renvoie celle qui contient la position de l'humain
- `getPatientsInChamber(Chamber chamber)` :
  - renvoie la liste des patients presents dans une chambre
- `administerRemedy(Doctor doctor, Patient patient, Remedy remedy, Date takenDate)` :
  - verifie que le medecin est vivant
  - verifie que le patient est vivant
  - verifie que le remede est dans le sac du medecin
  - verifie que medecin et patient sont dans la meme chambre
  - verifie qu'au moins une maladie soignee par le remede appartient aux specialites du medecin
  - enregistre la prise du remede chez le patient
  - applique l'effet du remede sur les maladies du patient
  - met a jour l'etat de vie du patient
  - retire le remede du sac du medecin

#### Pourquoi cette classe est importante

`Hospital` agit comme un petit service metier. Elle evite de mettre toute la logique dans l'interface graphique.

---

### `accessory/Remedy.java`

#### Role

Represente un remede ou medicament.

#### Attributs

- `finish` : date de fin de validite
- `marge` : marge de securite utilisee avant expiration
- `name` : nom du remede
- `price` : prix
- `cure` : tableau des maladies que le remede peut traiter

#### Constructeur

- `Remedy(Date finish, int marge, String name, int price, Disease[] cure)`

#### Methodes principales

- getters classiques sur tous les attributs
- `toString()` : retourne le nom

#### Utilisation

Un remede est :

- stocke dans le sac d'un medecin
- pris par un patient sous forme d'objet `Taken`
- utilise pour diminuer le niveau de certaines maladies

---

### `accessory/Taken.java`

#### Role

Represente une prise de remede par un patient.

#### Attributs

- `remedy` : le remede pris
- `date_take` : date de prise

#### Constructeur

- `Taken(Remedy remedy, Date date_take)`

#### Methodes principales

- `getRemedy()`
- `getDate_take()`

#### Utilisation

Cette classe sert d'historique simplifie des traitements pris par un patient.

---

## 5. Package `entity`

Ce package contient les objets du domaine medical et humain.

### `entity/Disease.java`

#### Role

Represente une maladie.

#### Attributs

- `id` : identifiant
- `name` : nom
- `transmission` : mode de transmission
- `mortal` : indique si la maladie peut tuer
- `contagious` : indique si la maladie est contagieuse

#### Constructeurs

- `Disease(String name, String transmission, Boolean mortal, boolean contagious, int id)`
- surcharge sans le champ `contagious`, qui vaut alors `false`

#### Methodes principales

- getters pour tous les attributs

#### Utilisation

Les objets `Disease` sont references :

- dans les specialites des medecins
- dans les maladies actives des patients
- dans les remedes

Le code compare souvent les maladies par reference objet avec `==`. Cela veut dire qu'il faut reutiliser le meme objet `Disease` partout pour que les correspondances fonctionnent.

---

### `entity/Human.java`

#### Role

Classe mere de toutes les personnes.

#### Attributs

- `DEFAULT_RADIUS` : rayon logique d'un humain pour les contacts
- `name`
- `birthday`
- `sexe`
- `pos` : position 2D
- `alive` : etat de vie

#### Constructeur

- `Human(String name, Date birthday, char sexe, Point2D.Double pos)`

#### Methodes principales

- getters classiques
- `isAlive()` : indique si la personne est vivante
- `getRadius()` : retourne le rayon logique
- `setName(...)`, `setBirthday(...)`, `setSexe(...)`, `setPos(...)`, `setAlive(...)`
- `move(double vx, double vy)` : deplacement relatif
- `moveTo(double x, double y)` : deplacement absolu
- `distanceTo(Human other)` : distance entre deux personnes
- `touches(Human other)` : vrai si deux personnes se touchent selon leur rayon

#### Point important

`setSexe(...)` contient une condition qui semble incorrecte :

- `if(sexe != 'm' || sexe != 'f')`

Cette condition est toujours vraie, car un caractere ne peut pas etre a la fois `m` et `f`.
Ensuite, la ligne `this.sexe = sexe;` remplace de toute facon la valeur precedente. Donc la protection ne fonctionne pas reellement.

---

### `entity/Doctor.java`

#### Role

Represente un medecin. Cette classe herite de `Human`.

#### Attributs

- `speciality` : tableau des maladies que le medecin sait traiter
- `bag` : tableau des remedes transportes

#### Constructeurs

- version sans sac de remedes
- version avec sac de remedes

#### Methodes principales

- `getSpeciality()`
- `getBag()`
- `setBirthday(Date birthday)` :
  - empeche en theorie un medecin trop jeune ou trop vieux
  - s'appuie sur `Date.age()`
- `setSpeciality(...)`
- `setBag(...)`
- `hasRemedy(Remedy remedy)` : verifie si le sac contient ce remede
- `removeRemedy(Remedy remedy)` : retire une occurrence du remede du sac
- `canTreat(Disease disease)` : verifie si la maladie fait partie des specialites
- `toString()` : retourne le nom du medecin

#### Utilisation

Un medecin est selectionnable dans l'interface. Si le medecin est dans une chambre, le panneau latéral lui permet de donner un remede a un patient de la meme chambre.

---

### `entity/Patient.java`

#### Role

Represente un patient. C'est la classe metier la plus riche du projet.

#### Attributs

- `previous` : maladies deja eues auparavant
- `diseases` : tableau des maladies courantes ou connues
- `levels` : niveau associe a chaque maladie dans `diseases`
- `congenital` : maladies congenitales
- `eaten` : historique des remedes pris

Le tableau `levels` est parallele au tableau `diseases` :

- `diseases[i]` est la maladie
- `levels[i]` est son niveau

#### Constructeur

Le constructeur initialise tous ces tableaux puis cree `eaten` comme tableau vide.

#### Methodes principales

- getters sur les differents tableaux
- `setLevels(int[] levels)` : modifie les niveaux
- `eat(Remedy[] to_eat, Date taken_date)` :
  - verifie la validite du remede via sa date de fin moins la marge
  - si le remede est acceptable, l'ajoute dans `eaten`
- `traitement_taking(Date taken_date)` :
  - tente de reconstruire la liste des traitements encore valides
  - methode assez complexe et fragile
- `current_disease()` :
  - retourne les maladies dont le niveau est strictement positif
- `hasActiveDisease(Disease disease)` :
  - vrai si le patient a cette maladie avec un niveau > 0
- `addDisease(Disease disease, int level)` :
  - ajoute une nouvelle maladie
  - ou reactive une maladie deja connue si son niveau etait <= 0
- `catchContagiousDiseasesFrom(Patient other)` :
  - copie les maladies contagieuses actives d'un autre patient
- `getDiseaseSummary()` :
  - produit une phrase lisible pour l'etat des maladies
- `applyRemedy(Remedy remedy)` :
  - parcourt les maladies soignees par le remede
  - diminue de 1 le niveau des maladies correspondantes
- `refreshLifeStatus()` :
  - si une maladie mortelle atteint le niveau `>= 7`, le patient meurt
- `toString()` : retourne le nom

#### Logique metier importante

Le projet represente l'etat de sante d'un patient par des niveaux entiers.

- niveau `<= 0` : maladie consideree comme inactive
- niveau `> 0` : maladie active
- niveau `>= 7` pour une maladie mortelle : mort du patient

L'administration d'un remede ne supprime pas directement une maladie. Elle diminue seulement son niveau.

---

### `entity/Visitor.java`

#### Role

Represente un visiteur.

#### Particularite

Cette classe n'ajoute pas de logique supplementaire. Elle sert surtout a distinguer visuellement et conceptuellement les visiteurs des autres humains.

---

## 6. Package `fenetre`

Ce package contient toute l'interface graphique Swing.

### `fenetre/PlanPalette.java`

#### Role

Centralise les couleurs du plan.

#### Contenu

On y trouve des constantes `Color` pour :

- l'arriere-plan
- la grille logique du plan
- le remplissage des chambres
- les bordures
- les couleurs des docteurs, patients, visiteurs, morts
- la couleur du texte

#### Interet

Cette classe evite de disperser des valeurs RGB dans tout le projet.

---

### `fenetre/ChamberPainter.java`

#### Role

Dessine les chambres sur le plan.

#### Methode principale

- `paint(Graphics2D g2, Hospital hospital, Dessin dessin)`

#### Fonctionnement

Pour chaque chambre :

1. recuperer le rectangle logique
2. convertir les coordonnees du monde vers l'ecran via `Dessin`
3. dessiner un rectangle rempli
4. dessiner sa bordure
5. ecrire le libelle `Chambre X`

Cette classe isole la responsabilite de dessin des chambres.

---

### `fenetre/HumanPainter.java`

#### Role

Dessine les personnes sur le plan.

#### Methode principale

- `paint(Graphics2D g2, Hospital hospital, Dessin dessin)`

#### Fonctionnement

Pour chaque humain :

1. recuperer sa position logique
2. la convertir en position ecran
3. dessiner un cercle representant la personne
4. choisir la couleur selon son type :
   - docteur
   - patient
   - visiteur
   - mort
5. dessiner son nom
6. dessiner une info inline :
   - maladies actives du patient
   - specialites du docteur

#### Methodes auxiliaires

- `resolveColor(Human human)` : choisit la couleur de fond
- `resolveBorderColor(Human human)` : choisit la bordure
- `drawInlineInfo(...)` : dessine un texte d'information
- `formatInlinePatient(Patient patient)` : resume les maladies actives
- `formatInlineDoctor(Doctor doctor)` : resume les specialites

Cette classe rend l'etat du modele visible sans cliquer sur les objets.

---

### `fenetre/Dessin.java`

#### Role

C'est la zone principale de dessin de l'hopital. La classe herite de `JPanel`.

#### Constantes

- `SCALE` : facteur d'agrandissement entre coordonnees logiques et pixels
- `MARGIN` : marge autour du plan
- `PERSON_SIZE` : taille graphique d'une personne

#### Attributs

- `hospital` : source des donnees
- `chamberPainter`
- `humanPainter`

#### Methodes principales

- `buildPreferredSize()` :
  - calcule la taille ideale du panneau
  - tient compte des chambres et des personnes
- `toWorld(Point point)` :
  - convertit un point ecran en coordonnees logiques
- `findChamberAt(Point point)` :
  - trouve la chambre sous un point ecran
- `findHumanAt(Point point)` :
  - trouve un humain proche d'un clic
- `toScreenX(double x)` et `toScreenY(double y)` :
  - convertissent du monde vers l'ecran
- `findElementAt(Point point)` :
  - renvoie l'humain clique si present
  - sinon la chambre
- `paintComponent(Graphics g)` :
  - methode Swing appelee automatiquement lors du rendu
  - delegue le dessin aux painter classes

#### Interet architectural

`Dessin` sert d'intermediaire entre :

- la logique metier de l'hopital
- la geometrie des objets
- le rendu graphique

---

### `fenetre/Ecoute.java`

#### Role

Gere les interactions souris sur la zone de dessin.

La classe herite de `MouseAdapter`.

#### Attributs

- `dessin`
- `status` : label d'etat en bas de la fenetre
- `controlPanel`

#### Methodes principales

- `mouseMoved(MouseEvent e)` :
  - convertit la position souris en coordonnees du monde
  - detecte si la souris est dans une chambre ou dans le couloir
  - met a jour le texte du label d'etat
- `mouseDragged(MouseEvent e)` :
  - reutilise `mouseMoved(...)`
- `mouseClicked(MouseEvent e)` :
  - detecte si un humain a ete clique
  - si oui, le transmet au `ControlPanel`
  - sinon deselectionne

#### Utilisation

Cette classe fait le lien entre un clic utilisateur et la notion metier d'humain selectionne.

---

### `fenetre/ControlPanel.java`

#### Role

Panneau lateral de controle. C'est lui qui permet les actions utilisateur.

#### Attributs principaux

- `hospital` : logique metier
- `dessin` : pour redessiner l'interface
- `selectedLabel` : affiche l'humain selectionne
- `xField`, `yField` : champs de saisie de la position
- `moveButton` : bouton de deplacement
- `medecineLabel` : texte d'etat du formulaire de remede
- `patientBox` : liste des patients disponibles
- `remedyBox` : liste des remedes du medecin
- `remedyButton` : bouton d'administration
- `selectedHuman` : humain actuellement selectionne

#### Construction de l'interface

Le constructeur :

1. memorise `hospital` et `dessin`
2. configure le layout
3. cree les composants Swing
4. ajoute les composants au panneau
5. branche les `ActionListener`
6. initialise l'etat du formulaire de remede

#### Methodes principales

- `setSelectedHuman(Human selectedHuman)` :
  - met a jour l'humain courant
  - actualise les champs de position
  - recharge le formulaire medecin
- `moveSelectedHuman()` :
  - lit les valeurs saisies
  - appelle `hospital.moveHumanTo(...)`
  - met a jour le texte et redessine
- `killSelectedHuman()` :
  - met `alive` a `false`
  - actuellement la fonctionnalite est desactivee dans l'interface
- `refreshDoctorForm()` :
  - vide les listes
  - si l'humain selectionne n'est pas un medecin, desactive le formulaire
  - si le medecin n'est pas dans une chambre, desactive aussi
  - sinon remplit la liste des patients de la chambre
  - remplit la liste des remedes du sac du medecin
- `administerRemedy()` :
  - lit les selections
  - appelle `hospital.administerRemedy(...)`
  - utilise une date fixe `23/04/2026`
  - rafraichit l'affichage si succes

#### Importance

Cette classe est le principal point de contact entre l'utilisateur et le modele.

---

### `fenetre/MaFenetre.java`

#### Role

Fenetre principale Swing.

#### Fonctionnement

Le constructeur :

1. definit le titre et la fermeture
2. cree `Dessin`
3. cree un label d'etat
4. cree `ControlPanel`
5. cree `Ecoute`
6. branche les ecouteurs souris sur `Dessin`
7. place les composants dans un `BorderLayout`
8. appelle `pack()`
9. centre la fenetre
10. la rend visible

#### Organisation visuelle

- centre : plan de l'hopital
- droite : panneau de controle
- bas : barre de statut

---

## 7. Package `main`

### `main/Main.java`

#### Role

Point d'entree du programme.

#### Ce que fait la methode `main`

1. cree plusieurs rectangles pour les chambres
2. construit les objets `Chamber`
3. cree plusieurs dates de test
4. cree plusieurs maladies
5. cree plusieurs remedes
6. cree plusieurs patients avec leurs maladies et niveaux
7. appelle `refreshLifeStatus()` sur les patients
8. force `p3.setAlive(true)` apres recalcul, ce qui signifie qu'on annule manuellement un eventuel deces
9. cree des medecins avec leurs specialites et leurs sacs de remedes
10. cree un visiteur
11. remplit les `Vector` de personnes et de chambres
12. construit l'objet `Hospital`
13. lance l'interface via `SwingUtilities.invokeLater(...)`

#### Pourquoi ce fichier est important

Tout le scenario initial est defini ici. Si vous voulez changer :

- les positions
- les chambres
- les maladies
- les remedes
- les patients
- les medecins

alors c'est principalement dans ce fichier qu'il faut intervenir.

---

## 8. Scripts et fichiers annexes

### `run.ps1`

#### Role

Script PowerShell pour compiler et executer le projet sous Windows.

#### Fonctionnement

- supprime les anciennes classes
- cree le dossier `.bin` si necessaire
- recupere tous les fichiers `.java`
- compile avec `javac -d .bin`
- lance la classe principale avec `java -cp .bin main.Main`

#### Interet

Ce script est plus propre que de compiler a la main fichier par fichier.

---

### `run.sh`

#### Role

Version shell Unix tres courte pour compiler et executer.

#### Fonctionnement

- compile tous les `.java` vers le dossier courant
- execute `main.Main`

#### Limite

Ce script est plus simple, mais moins robuste que `run.ps1`.

---

### `sujet.txt`

#### Role

Notes de travail ou brouillon de consignes.

#### Contenu

On y trouve des idees de fonctionnalites et de regles, par exemple :

- identifiant de maladie
- niveau de maladie
- specialites du doctor
- regles sur les niveaux
- ajout de coordonnees 2D
- chambres rectangulaires
- deplacement des docteurs

Ce fichier ressemble a un pense-bete de conception du projet.

---

## 9. Relations entre les classes

### Modele metier

- `Hospital` contient des `Human` et des `Chamber`
- `Doctor`, `Patient`, `Visitor` heritent de `Human`
- `Patient` contient des `Disease`, des niveaux et des `Taken`
- `Taken` contient un `Remedy` et une `Date`
- `Remedy` contient des `Disease` qu'il peut soigner
- `Doctor` contient des specialites `Disease[]` et un sac `Remedy[]`

### Interface graphique

- `MaFenetre` cree `Dessin`, `ControlPanel` et `Ecoute`
- `Dessin` utilise `ChamberPainter` et `HumanPainter`
- `Ecoute` parle a `ControlPanel`
- `ControlPanel` appelle les methodes de `Hospital`

---

## 10. Scenario fonctionnel actuel

Quand le programme demarre :

- plusieurs chambres sont visibles
- plusieurs personnes sont placees sur le plan
- les patients affichent leurs maladies actives
- les medecins affichent leurs specialites

Quand l'utilisateur interagit :

1. il clique sur une personne
2. le panneau de droite affiche ses informations
3. il peut modifier `X` et `Y` pour la deplacer
4. si la personne est un medecin :
   - le panneau affiche les patients de la meme chambre
   - affiche les remedes disponibles
   - permet d'administrer un remede
5. si le remede est applicable :
   - le niveau de certaines maladies diminue
   - le remede quitte le sac du medecin
   - l'affichage se met a jour

---

## 11. Points forts du code

- La separation en packages est claire.
- L'heritage `Human -> Doctor/Patient/Visitor` est simple et logique.
- La logique d'hopital est centralisee dans `Hospital`.
- Le dessin est bien separe entre chambres et humains.
- L'interface permet deja une vraie interaction utilisateur.

---

## 12. Limites et points a surveiller

Voici les points les plus importants si vous voulez faire evoluer le projet.

### 1. Utilisation de `Vector` sans generiques

Le code manipule des `Vector` bruts, par exemple `Vector personne`.
Cela oblige a faire beaucoup de casts et reduit la securite de typage.

Exemple d'amelioration :

- `Vector<Human>`
- `Vector<Chamber>`

### 2. Comparaison des objets par `==`

Plusieurs classes comparent les maladies ou remedes par reference memoire.
Cela marche seulement si on reutilise exactement les memes objets.

### 3. Dates mutables

`Patient.eat(...)` modifie la date de fin du remede en appelant `add_day(...)`.
Comme l'objet `Date` est mutable, cela peut alterer l'etat du remede lui-meme de facon durable.

### 4. Verification du sexe dans `Human`

La condition actuelle semble erronee et ne protege pas reellement les valeurs invalides.

### 5. Dates fixes dans le code

Certaines dates sont codees en dur :

- `Date.age()` utilise `27/03/2026`
- `ControlPanel.administerRemedy()` utilise `23/04/2026`

Cela limite le realisme et la reutilisabilite.

### 6. Quelques methodes encore fragiles

`traitement_taking(...)` est difficile a suivre et semble sensible aux erreurs d'index.

---

## 13. Conseils pour continuer le projet

Si vous voulez ameliorer le code, les prochaines etapes les plus utiles seraient :

1. remplacer les `Vector` bruts par des collections generiques
2. corriger la classe `Date` ou utiliser `java.time.LocalDate`
3. corriger `Human.setSexe(...)`
4. rendre la gestion des traitements plus fiable
5. separer encore mieux la logique metier et l'interface
6. ajouter des actions automatiques, par exemple contagion quand deux patients se touchent

---

## 14. Resume final

Ce projet est une simulation graphique simple d'hopital.

Le coeur du modele est base sur :

- des personnes placees dans un espace 2D
- des chambres rectangulaires
- des maladies avec niveaux
- des remedes administres par des medecins

Le coeur technique du projet repose surtout sur :

- `Hospital` pour les regles globales
- `Patient` pour la logique de maladie
- `ControlPanel` pour les actions utilisateur
- `Dessin` et les painter classes pour l'affichage

Si vous ouvrez ce projet pour le comprendre rapidement, commencez dans cet ordre :

1. `main/Main.java`
2. `accessory/Hospital.java`
3. `entity/Human.java`, `Doctor.java`, `Patient.java`
4. `fenetre/MaFenetre.java`, `Dessin.java`, `ControlPanel.java`

