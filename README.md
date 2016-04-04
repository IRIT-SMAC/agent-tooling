# Agent-tooling
[![Build Status](https://travis-ci.org/IRIT-SMAC/agent-tooling.svg?branch=master)](https://travis-ci.org/IRIT-SMAC/agent-tooling)
[![Coverage Status](https://coveralls.io/repos/IRIT-SMAC/agent-tooling/badge.svg?branch=master&service=github)](https://coveralls.io/github/IRIT-SMAC/agent-tooling?branch=master)

- [Github] (https://github.com/IRIT-SMAC/agent-tooling)
- [Travis] (https://travis-ci.org/IRIT-SMAC/agent-tooling)
- [Coveralls] (https://coveralls.io/github/IRIT-SMAC/agent-tooling)

**/!\ Agent-tooling est en maintenance, des changements risquent d’avoir lieu.**

Agent-tooling est un ensemble de librairies développées dans le but de faciliter le développement de systèmes multi-agents.

Dans chaque librairie, le package “example” contient des exemples d’utilisation de la librairie en question.

## Agent-scheduling
** Agent-scheduling gère l’exécution des agents. **

Deux stratégies d'exécution des agents sont actuellement implémentées.

* **SynchronizedSystem:**

Dans cette stratégie, l'exécution d'un agent se fait en une seule étape. Les agents doivent donc implémenter une méthode nextStep().  
Une étape du système correspond à une exécution en parallèle d'une étape de tous les agents.  
Pour exécuter l'étape suivante du système, il faut attendre que tous les agents aient terminé leur étape précédente.

* **TwoStepsSystemStrategy :** 

Dans cette stratégie, l'exécution d'un agent se fait en deux étapes.
  1. Etape de perception des agents : Tous les agents exécutent en parallèle leur perception. Cette étape est terminée lorsque tous les agents l'ont terminée. Les agents doivent donc implémenter une méthode perceive().
  2. Etape de décision et action des agents :  Tous les agents exécutent en parallèle leurs décisions et leurs actions. Les agents doivent donc implémenter une méthode decideAndAct().

** Pour les deux stratégies :**
 * Une étape d'un agent est une exécution asynchrone.
 * Une étape du système est une exécution synchrone.


## Agent-messaging
** Agent-messaging permet aux agents de communiquer entre eux. **

Dans cette librairie, les agents communiquent par messages. Un message est envoyé dans la boîte aux lettres du ou des agents concerné(s).

Un agent peut :  
* Envoyer un message à un autre agent.  
* Diffuser un message à tous les agents.  
* Envoyer un message à son groupe.  
* Faire partie d’un groupe.  

## Agent-plot
** Agent-plot permet d'afficher des valeurs sous forme de courbes. **

Ainsi, dans le cas d'un SMA, on peut visualiser l'évolution des valeurs des attributs des agents.

## Agent-logging
** Agent-logging permet de tracer l'exécution des agents sous forme de logs. **

Un fichier de log est crée pour chaque agent dans un dossier log.  
Les logs doivent être implémentés pour chaque agent. Par exemple, les logs peuvent être implémentés dans l’étape perceive d’un agent.

## AVT

**AVT est un outil utilisé pour s'approcher au mieux d'une valeur qui peut varier dans le temps sur la base de feedbacks "plus grand", "plus petit".**