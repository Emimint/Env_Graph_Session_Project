# Env_Graph_Session_Project
## Project de session Ete 2023

Ce systeme de gestion de locations a été réalisé comme projet de session [AEC Développement Web Collège Rosemont ](https://www.crosemont.qc.ca/programme/programmation-orientee-objet-et-technologies-web/) par les étudiantes suivantes :
-  [Cylia Oudiai](https://www.linkedin.com/in/cylia-oudiai-81b7891a0/)
- Tity Doupamby
- [Emilie Echevin ](https://www.linkedin.com/in/emilie-echevin/)

Pour pouvoir utiliser le projet localement, voici les étapes à suivre :

1. Aller dans le répertoire `Project\src\data\input` et récupérer le fichier  `gestion_location_db.sql`. Il s'agit de la base de données utilisée dans le projet.
2. Installer la base de données sur votre machine (avec MySQL Workbench par exemple). Il y a deux tables: la table 'locations' (extrait ci-dessous) :
   
![image](https://github.com/Emimint/Env_Graph_Session_Project/assets/90863470/13951186-4f72-4fd5-b89f-2e6b348cdd59)

et la table 'utilisateurs' :

![image](https://github.com/Emimint/Env_Graph_Session_Project/assets/90863470/ae792dcb-4570-4694-8426-88f97b1591f7)

**ATTENTION!!** : 
Il faut s'assurer que dans le fichier `Project/src/main/java/com/example/project/models/MySqlConnection.java`, le mot de passe et le nom d'utilisateur correspondent au paramètre de votre machine :

![image](https://github.com/Emimint/Env_Graph_Session_Project/assets/90863470/21dd31d2-7574-4dfe-a48e-401c8107fe67)

(__Autrement, vous aurez un message d'erreur en console.__)

3. Une fois connecté, vous pouvez sélectionner n'importe quel utilisateur parmis les six disponibles pour accéder au système.


