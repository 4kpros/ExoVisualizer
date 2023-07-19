# ExoVisualizer

Petit test de decodage audio et d'affichage d'un "visualiseur" grace a **la transformation de fourier**

Test ok et decodage plutot rapide.

Comment l'utiliser ?

- Cloner
- Importer dans Android Studio et attendre la fin d'installation des dependances
- Lancer l'application (Vous aurez un graphique de frequences(sinusoïdal ou un truc similaire).
- Pour changer le clip, importez votre fichier audio(dans n'importe quel format dans le repertoire raw des ressources.

Dans mon cas c'est le fichier **sample_96khz_24bit.flac**. Il contient de hautes frequences donc ca peut faire un effet bizare(telechargez https://github.com/4kpros/ExoVisualizer/blob/main/app/src/main/res/raw/sample_96khz_24bit.flac et jouez le dans votre lecteur pour ressentir cet effet)

A noter que cette demo utilise le player **Exoplayer** au lieu du Mediaplayer par defaut
