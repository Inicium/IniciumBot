# IniciumBot
Bot musique pour Discord<br/>
Pour avoir un accès hébergé au bot, merci de me le demander

# Présentation
## Table des matières
- [Commandes gestion musique](#commandes-gestion-musique)
  - `(new)`[Playlist](#playlist) 
  - [Play](#play)
  - [Skip](#skip)
  - [PlaySkip](#playskip)
  - [Pause / Resume](#pause--resume)
  - [Seek](#seek)
  - [Disconnect](#disconnect)
  - [Clear](#clear)
  - [Queue](#queue)
  - [Shuffle](#shuffle)
  - [MoveAll](#moveall)
- [Commandes générales](#commandes-générales)
  - [MoveAll](#moveall)
  - [Help](#help)
- [Commandes administrateur](#commandes-administrateur)
  - [Blacklist](#blacklist)
  - [Welcome](#welcome)
  - [Goodbye](#goodbye)
  - [HelpAdmin](#helpadmin)

# Commandes gestion musique
## Playlist
### Description
Lancer, supprimer ou ajouter une musique sur la playlist d'un serveur Discord
### Commande
/playlist</br>
/playlist `[action]`
### Paramètre
`[action]` : `add` ou `remove` pour ajouter ou supprimer</br>
Facultatif, si rien n'est renseigné, ouvre la playlist (équivalent au bouton suivant)
### Bouton
![image](https://github.com/user-attachments/assets/a17448f0-617b-4c3c-a448-2282ee0b1cac)
### Exemples
#### Afficher la playlist
```/playlist```</br></br>
![image](https://github.com/user-attachments/assets/c6c47b0b-6774-4603-8b71-661b43291f08)
#### Ajouter un musique à la playlist
```/playlist action: add```</br></br>
![image](https://github.com/user-attachments/assets/7460a208-98f0-4645-a984-359b4bf92220)
#### Supprimer des musiques de la playlist
```/playlist action: remove```</br></br>
![image](https://github.com/user-attachments/assets/5f3d761d-757b-41e5-be9a-4e951cc6c23e)
## Play
### Description
Jouer une piste sur le channel vocal (YouTube, Twitch, SoundCloud, ...)
### Commande
/play `[musique]`</br>
/p `[musique]`
### Paramètre
`[musique]` : Lien/Recherche de la musique
### Exemples
#### ➡️ Avec un lien Youtube
```/play musique: https://www.youtube.com/watch?v=LuGAWR2eRyQ```</br></br>
![image](https://user-images.githubusercontent.com/28084726/166140199-fb2c8de4-6e15-4538-8fa2-69c20ba4a22a.png)
#### ➡️ Avec une recherche youtube
```/play musique: djomb wow```</br></br>
![image](https://user-images.githubusercontent.com/28084726/166140293-59c509c9-b505-4e89-95f4-d500091b2b7a.png)
#### ➡️ Avec une playlist Youtube ⚠️publique ou non répertoriée⚠️
```/p musique: https://www.youtube.com/watch?v=cDSFtfz3o5A&list=PLKwY3kIzGxRm9sDhYDDZAqm5Q9OO_2M5i```</br></br>
![image](https://user-images.githubusercontent.com/28084726/166140761-beea4b57-9baa-49fe-b92d-4affba9700b4.png)
#### ➡️ Avec un lien Twitch
```/play musique: https://www.twitch.tv/duckvilleusa```</br></br>
![image](https://user-images.githubusercontent.com/28084726/166140353-786ab4b6-36ed-4991-bd80-c2d26b33c291.png)
#### ➡️ Avec un flux radio
Récupérer le lien d'un flux radio mp3 qui fonctionne via le site [Flux Radios](https://fluxradios.blogspot.com/)</br>
Pour NRJ par exemple</br></br>
![image](https://user-images.githubusercontent.com/28084726/166140454-8724d87e-4a6d-4adb-9ec0-f514c82edc71.png)
![image](https://user-images.githubusercontent.com/28084726/166140520-505d32f3-e243-4e36-ac04-1d857de309cd.png)
</br></br>```/p musique: http://scdn.nrjaudio.fm/adwz2/fr/30001/mp3_128.mp3?origine=fluxradios```</br></br>
![image](https://user-images.githubusercontent.com/28084726/166140497-c25e05dd-4ec5-4b82-a71f-ecdda45e31b9.png)

## Skip
### Description
Passer la lecture en cours pour jouer la musique suivante
### Commande
/skip</br>
/s
### Bouton
![image](https://user-images.githubusercontent.com/28084726/166140956-939676dc-fdd7-42e0-8eb5-2b23a53a44ec.png)
### Exemple
![image](https://user-images.githubusercontent.com/28084726/166140814-6190d763-a402-4581-bb70-d2ee5a3e12b9.png)
</br></br>```/skip```</br></br>
![image](https://user-images.githubusercontent.com/28084726/166140852-46a893ee-ee38-45b6-b19d-4e9784656c81.png)

## PlaySkip
### Description
Lancer la lecture d'une musique avec un lien ou une recherche et passer la musique en cours
### Commande
/ps `[musique]`
### Paramètre
`[musique]` : Lien/Recherche de la musique

## Pause / Resume
### Description
Mettre en pause la musique en cours de lecture / Relancer la musique mise en pause
### Commande
/pause</br>
/resume
### Bouton
![image](https://user-images.githubusercontent.com/28084726/166141265-af44f026-889f-4ba1-a6d4-19e433918fb9.png)
![image](https://user-images.githubusercontent.com/28084726/166141305-588876b6-c481-4679-ba8a-1121a28fc79a.png)
### Exemple
```/pause```</br></br>
![image](https://user-images.githubusercontent.com/28084726/166141180-289f5966-01e7-4837-b645-5d25f70d6f57.png)</br></br>
```/resume```</br></br>
![image](https://user-images.githubusercontent.com/28084726/166141272-d9ad4106-41ec-4398-a555-0a76b6318995.png)

## Seek
### Description
Avancer / Reculer le temps de lecture de la musique en cours
### Commande
/seek `[temps]`
### Paramètre
`[temps]` : Temps sous forme HH:MM:SS ou MM:SS
### Exemple
```/seek time: 1:00```</br></br>
![image](https://user-images.githubusercontent.com/28084726/166141425-1aaea30f-eda7-4ee7-abba-4223e2dde923.png)

## Disconnect
### Description
Déconnecter le bot du channel audio
### Commande
/disconnect</br>
/leave</br>
/quit</br>
/dc
### Bouton
![image](https://user-images.githubusercontent.com/28084726/166141828-5e1ed67f-23a6-40ee-9167-54c4aa59f64b.png)
### Exemple
```/disconnect```</br></br>
![image](https://user-images.githubusercontent.com/28084726/167289042-ef074e9a-dc26-4962-9c42-fc11993c9dd8.png)

## Clear
### Description
Effacer la liste des musiques en attente
### Commande
/clear</br>
/clean</br>
/clr
### Bouton
![image](https://user-images.githubusercontent.com/28084726/167289138-d62b546e-3e92-4dee-a2ae-97c36e3c4f17.png)
### Exemple
![image](https://user-images.githubusercontent.com/28084726/167289147-8a4146fd-fea1-4cf7-b792-5bde16de52ce.png)</br></br>
```/clear```</br></br>
![image](https://user-images.githubusercontent.com/28084726/167289179-45fbbef8-b15a-4e6c-b1a0-d6747df0066a.png)

## Queue
### Description
Voir la liste de lecture
### Commande
/queue</br></br>
/np
### Exemple
```/np```</br></br>
![image](https://user-images.githubusercontent.com/28084726/167289241-d44ab378-902e-43f1-86ce-596a50af83c9.png)

## Shuffle
### Description
Mélanger la liste de lecture
### Commande
/shuffle
### Bouton
![image](https://user-images.githubusercontent.com/28084726/167289263-30aede7e-fd93-4946-ade0-559ab62a359e.png)
### Exemple
![image](https://user-images.githubusercontent.com/28084726/167289400-b7a12574-d760-46ea-b58a-25e50b0cb73a.png)</br></br>
```/shuffle```</br></br>
![image](https://user-images.githubusercontent.com/28084726/167289423-c5d34c88-38fc-48bf-992a-140a2cafe19b.png)

# Commandes générales

## MoveAll
### Description
Déplacer tous les utilisateurs du channel actuel
### Commande
/moveall [destination]</br></br>
/mva [destination]
### Paramètres
`[destination]` : Channel de destination
### Exemple
![image](https://user-images.githubusercontent.com/28084726/167289591-517c3a69-dc2f-4714-bb97-ed08c3b988e5.png)</br></br>
```/mva destination: #🥇 Salle de discussion 1```</br></br>
![image](https://user-images.githubusercontent.com/28084726/167289616-5937a7b6-12b2-495a-865f-170e3d482a83.png)</br></br>
![image](https://user-images.githubusercontent.com/28084726/167289628-03cf541e-4391-4a13-83bc-92a436b2c77f.png)

## Help
### Description
Voir la liste des commandes du bot musique
### Commande
/help
### Exemple
```/help```</br></br>
![image](https://user-images.githubusercontent.com/28084726/167289669-cc5bb189-5212-4387-b814-723db88791af.png)

# Commandes administrateur
## Blacklist
### Description
Définir les channels où l'on ne peut pas executer de commande
### Commande
/blacklist
### Exemple
```/blacklist```</br></br>
![image](https://user-images.githubusercontent.com/28084726/167290049-593232f3-c4c8-42e8-bae7-a76cd516ada9.png)

## Welcome
### Description
Définir un channel pour l'affichage d'un message de bienvenue
### Commande
/welcome
### Exemple
```/welcome```</br></br>
![image](https://user-images.githubusercontent.com/28084726/167289797-d64cec78-a3b6-41f9-a984-1ecc72334968.png)

## Goodbye
### Description
Définir un channel pour l'affichage d'un message lors d'un départ
### Commande
/goodbye
### Exemple
```/goodbye```</br></br>
![image](https://user-images.githubusercontent.com/28084726/167289820-6ff94751-f2e5-4151-b402-41a61dbf24a5.png)

## HelpAdmin
### Description
Voir la liste des commandes administrateur
### Commande
/helpadmin
### Exemple
```/helpadmin```</br></br>
![image](https://user-images.githubusercontent.com/28084726/167289698-3042fec4-f458-4cc6-8396-6cadf6d19788.png)

# Screenshots
![image](https://user-images.githubusercontent.com/28084726/167290097-1c4755ae-9352-4651-b4d2-5ba95054d377.png)
![image](https://user-images.githubusercontent.com/28084726/167290108-b0b627a8-1ccc-40ef-b348-4a680ed57965.png)
![image](https://github.com/user-attachments/assets/040bffdc-23f0-4b1e-b73a-51fab8991e24)
