# Echoes of the Abyss

**Echoes of the Abyss** est un jeu 2D action rogue-lite développé avec [libGDX](https://libgdx.com/). Le joueur explore un donjon mystérieux rempli d’ennemis, avec pour objectif de progresser étage après étage tout en collectant des ressources et en renforçant son personnage.

## 📋 Prérequis

- **OS** : Windows 10 ou Linux  
- **Carte graphique** : NVIDIA GeForce RTX 2070 ou équivalent  
- **Processeur** : Intel Core i5-8300H ou équivalent  
- **Java** : JDK 8 ou supérieur  
- **Gradle** : Intégré avec le projet, aucun besoin d'installation externe  

## 🚀 Installation

1. **Cloner le dépôt** :
   ```bash
   git clone <repository_url>
   cd echoes-of-the-abyss
2. **Nettoyer les fichiers précédents (optionnel) :** :
   Si vous souhaitez repartir sur une base propre, exécutez la commande suivante :
   ```bash
   ./gradlew clean
3. **Construire le projet** :
   Utilisez le wrapper Gradle intégré :
   ```bash
   ./gradlew build
4. Générer un fichier exécutable :
   Pour créer un exécutable `.jar` qui contient toutes les ressources nécessaires au jeu, exécutez la commande suivante :
    ```bash
    ./gradlew createExecutables
 
5. **Lancer le jeu** :
   Sur Windows, exécutez `launch_game.bat` dans le dossier dist/windows.
   Sur macOS ou Linux, exécutez launch_game.sh dans le dossier correspondant : 
    ```bash
   ./launch_game.sh
   
## 🛠️ Structure du projet

- **core** : Contient la logique principale et les mécaniques de jeu, partagées entre toutes les plateformes.
- **lwjgl3** : Plateforme principale pour desktop, basée sur LWJGL3.

## 📦 Déploiement

- **Exécutable Desktop** :
    - Le fichier `.jar` généré dans `lwjgl3/build/libs` peut être utilisé pour distribuer le jeu.
    - Assurez-vous que le système cible dispose de Java 8 ou supérieur installé.

- **Configuration multiplateforme** : Le projet est extensible pour d'autres plateformes (mobile, navigateur) via les modules supplémentaires de libGDX.

## 🧪 Gradle - Tâches Utiles

- `build` : Compile le code source et génère les fichiers nécessaires.
- `clean` : Supprime les dossiers `build`.
- `test` : Exécute tout les tests unitaires.
- `run` : Exécute directement le jeu.
- `test run` : Exécute les tests et lance le jeu uniquement si les différents tests unitaires passent.
- `createExecutables` : Génère des exécutables pour Windows, macOS et Linux.

## 🎮 À propos du jeu

- **Titre** : Echoes of the Abyss
- **Genre** : Rogue-lite, Beat 'em all, Action 2D
- **Plateforme cible** : PC
- **Style graphique** : Pixel art 2D sombre et immersif
- **Objectif principal** : Descendre le plus profondément possible dans un donjon rempli d'ennemis et de défis.  
