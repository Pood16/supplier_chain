# ATELIER : Agent Marketing Automatisé

> **Objectif** : Automatiser une campagne de marketing digital
>
> L'agent va lire les véhicules en vente et les clients dans Google Sheets, puis envoyer des emails personnalisés via Gmail.

---

## Vue d'ensemble de l'atelier

```
┌─────────────────────────────────────────────────────────────────┐
│                  WORKFLOW MARKETING AUTOMATION                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────┐    ┌───────────┐                                  │
│  │   Chat   │───▶│ AI Agent  │                                 │
│  │ Trigger  │    │           │                                  │
│  └──────────┘    └─────┬─────┘                                  │
│                        │                                        │
│            ┌───────────┼───────────┐                           │
│            │           │           │                           │
│            ▼           ▼           ▼                           │
│     ┌──────────┐ ┌──────────┐ ┌──────────┐                    │
│     │  OpenAI  │ │  Memory  │ │  Tools   │                    │
│     │  GPT-4o  │ │          │ │          │                    │
│     └──────────┘ └──────────┘ └────┬─────┘                    │
│                                     │                          │
│                    ┌────────────────┼────────────────┐        │
│                    │                │                │        │
│                    ▼                ▼                ▼        │
│              ┌──────────┐    ┌──────────┐    ┌──────────┐    │
│              │  Sheets  │    │  Sheets  │    │  Gmail   │    │
│              │Véhicules │    │ Clients  │    │  Send    │    │
│              └──────────┘    └──────────┘    └──────────┘    │
│                                                                │
└─────────────────────────────────────────────────────────────────┘
```

---

## Étape 1 : Installation et Configuration de n8n

### Option 1 : n8n Cloud (Le Plus Simple)

1. **Créez un compte** sur [n8n.io](https://n8n.io)
2. **14 jours d'essai gratuit**
3. **Aucune installation requise**
4. **Idéal pour** : Tests rapides, petites équipes

## Étape 2 : Préparation des données source

### Créer le Google Sheet

Créez un fichier Google Sheet nommé **"Marketing Auto"** avec **deux feuilles** :

### Feuille 1 : "voitures"

| Marque     | Modèle   | Couleur | Puissance | Prix   | A_PROMOUVOIR |
| ---------- | -------- | ------- | --------- | ------ | ------------ |
| Dacia      | Logan    | Blanc   | 90 CV     | 135000 | Oui          |
| Renault    | Clio     | Gris    | 100 CV    | 185000 | Non          |
| Peugeot    | 208      | Noir    | 110 CV    | 220000 | Oui          |
| Volkswagen | Golf     | Bleu    | 150 CV    | 320000 | Non          |
| Toyota     | Corolla  | Blanc   | 140 CV    | 280000 | Oui          |
| Hyundai    | Tucson   | Rouge   | 185 CV    | 380000 | Non          |
| Fiat       | Tipo     | Gris    | 95 CV     | 175000 | Oui          |
| Citroën    | C3       | Orange  | 82 CV     | 165000 | Non          |
| Kia        | Sportage | Noir    | 177 CV    | 350000 | Oui          |
| Mercedes   | Classe A | Argent  | 163 CV    | 450000 | Non          |

> ⚠️ **Important** : La colonne **"A_PROMOUVOIR"** contient `Oui` pour les véhicules à inclure dans la campagne, `Non` pour les exclure.

### Feuille 2 : "clients"

| Nom        | Prénom   | Email                             |
| ---------- | -------- | --------------------------------- |
| Abdessetar | Mohammed | elyagoubiabdessattarmoh@gmail.com |
| Hammaoui   | Anas     | anashammaoui07@gmail.com          |
| Benkhira   | Karim    | karim.benkhiraa@gmail.com         |
| Chadli     | Imane    | imanechadli2001@gmail.com         |

---

## Étape 3 : Configuration de l'environnement n8n

### 3.1 Créer le Workflow

1. Connectez-vous à n8n
2. Cliquez sur **"+ New Workflow"**
3. Nommez-le : **"Agent Marketing Campagne Auto"**

### 3.2 Ajouter le Trigger

1. Cliquez sur **"+"** pour ajouter un nœud
2. Recherchez **"Chat Trigger"** (ou "On Chat Message")
3. Placez-le sur le canvas

> Ce trigger permet d'interagir avec l'agent via une interface de discussion.

---

## Étape 4 : Construction du "Cerveau" de l'agent

### 4.1 Ajouter le noeud AI Agent

1. Cliquez sur **"+"** à droite du Chat Trigger
2. Recherchez **"AI Agent"**
3. Connectez-le au trigger

### 4.2 Connecter le modèle de langage (LLM)

1. Dans le nœud AI Agent, cliquez sur **"+ Add Chat Model"**
2. Sélectionnez **"OpenAI Chat Model"**

### 4.3 Ajouter la mémoire

1. Cliquez sur **"+ Add Memory"**
2. Sélectionnez **"Simple Memory"**
3. Configuration :

```
Context Window Length: 10
Session ID Key: sessionId
```

> La mémoire permet à l'agent de se souvenir de votre nom et du contexte de la conversation.

---

## Étape 5 : Configuration des Outils (Tools)

L'agent a besoin de **"bras"** pour agir. Connectez ces outils au nœud AI Agent :

### 5.1 Outil Google Sheets - voitures

1. Dans AI Agent, cliquez sur **"+ Add Tool"**
2. Sélectionnez **"Google Sheets Tool"**
3. Configuration :

```
Name: consulter_voitures_en_vente
Description: Consulter la liste des voitures

Credential: [Votre Google Sheets OAuth2]
Tool Description: Set Manually
Operation: Get Rows
Document: Marketing Auto
Sheet: voitures
Options:
  - Filters: A_PROMOUVOIR = Oui
```

> ⚠️ **Crucial** : La description du tool est TRÈS importante ! C'est elle qui permet à l'agent de comprendre quand et comment utiliser cet outil.

### 5.2 Outil Google Sheets - Clients

1. **"+ Add Tool"** → **"Google Sheets Tool"**
2. Configuration :

```
Name: consulter_liste_clients
Description: Récupérer la liste complète des clients.

Credential: [Votre Google Sheets OAuth2]
Tool Description: Set Manually
Operation: Get Rows
Document: Marketing Auto
Sheet: clients
```

### 5.3 Outil Gmail - Envoi d'emails

1. **"+ Add Tool"** → **"Gmail Tool"**
2. Configuration :

```
Name: envoyer_email_marketing
Description: Envoyer un email marketing aux clients.
Credential: [Votre Gmail OAuth2]
Operation: Send Email
Email Type: HTML

Options:
  ✅ Auto-generate subject (l'IA rédige le sujet)
  ✅ Auto-generate message (l'IA rédige le contenu)
```

---

## Étape 6 : Rédaction du Message Système (Prompt)

Dans les paramètres du nœud AI Agent, configurez le **System Message** :

````markdown
Vous êtes un assistant de marketing digital professionnel pour un concessionnaire automobile.

## VOTRE MISSION :

Lorsqu'on vous demande de lancer la campagne marketing, vous devez :

1. Consulter la liste des véhicules sélectionnés pour la vente (A_PROMOUVOIR=Oui)
2. Récupérer la liste complète des clients
3. Envoyer un email personnalisé et élégant à CHAQUE client

## FORMAT DES EMAILS :

- **Sujet** : Accrocheur (ex: "Nos meilleures offres automobiles !")
- **Corps** : Format HTML professionnel avec :
  - Salutation personnalisée avec le prénom du client
  - Introduction accrocheuse
  - Liste à puces des véhicules disponibles avec :
    • Marque et modèle
    • Année
    • Prix en Dirhams (DH)
  - Appel à l'action (CTA)
  - Signature professionnelle

## RÈGLES IMPORTANTES :

1. Toujours utiliser le prénom du client dans la salutation
2. Formater les prix avec le symbole DH et des espaces pour la lisibilité
3. Être professionnel mais chaleureux dans le ton
4. Confirmer chaque email envoyé avec le nom du destinataire

## EXEMPLE DE STRUCTURE EMAIL :

```html
<h2>Bonjour [Prénom] !</h2>
<p>Découvrez nos offres exceptionnelles du moment...</p>
<ul>
  <li><strong>Peugeot 208</strong> (2022) - 150 000 DH</li>
  ...
</ul>
<p><strong>Contactez-nous vite !</strong></p>
```
````

---

## Étape 7 : Architecture finale du Workflow

Votre workflow devrait ressembler à ceci :

```
┌─────────────────┐      ┌─────────────────────────────────────────┐
│  Chat Trigger   │─────▶│              AI Agent                   │
│                 │      │                                         │
│  "On Chat       │      │  System Message:                        │
│   Message"      │      │  "Vous êtes un assistant de             │
│                 │      │   marketing digital..."                 │
└─────────────────┘      │                                         │
                         │  ┌─────────────────────────────────┐   │
                         │  │ OpenAI Chat Model               │   │
                         │  │ • Model: gpt-4o                 │   │
                         │  │ • Temperature: 0.7              │   │
                         │  └─────────────────────────────────┘   │
                         │                                         │
                         │  ┌─────────────────────────────────┐   │
                         │  │ Simple Memory                   │   │
                         │  │ • Window: 10 messages           │   │
                         │  └─────────────────────────────────┘   │
                         │                                         │
                         │  ┌─────────────────────────────────┐   │
                         │  │ Tools:                          │   │
                         │  │ • Sheets: consulter_vehicules│   │
                         │  │ • Sheets: consulter_clients  │   │
                         │  │ • Gmail: envoyer_email       │   │
                         │  └─────────────────────────────────┘   │
                         └─────────────────────────────────────────┘
```

---

## Étape 8 : Exécution et Test

### 8.1 Lancer le chat

1. Cliquez sur **"Chat"** dans le panneau de droite de n8n
2. La fenêtre de conversation s'ouvre

### 8.2 Tester l'agent

**Conversation exemple :**

---

**Vous :** Lance la campagne marketing

**Agent :** (Le cycle React s'enclenche)

```
REASONING: Je dois d'abord consulter les voitures disponibles à la vente...

ACTION: Appel de l'outil "consulter_voitures_en_vente"

OBSERVATION:

REASONING: J'ai 5 voitures. Je dois maintenant récupérer la liste des clients...

ACTION: Appel de l'outil "consulter_liste_clients"

OBSERVATION:

REASONING: J'ai 2 clients. Je vais maintenant envoyer un email personnalisé à chacun...

ACTION: Envoi email à abdelaziz mdidech
OBSERVATION: Email envoyé avec succès

ACTION: Envoi email à didech abdel
OBSERVATION: Email envoyé avec succès
```

**Réponse finale de l'agent :**

✅ **Campagne marketing lancée avec succès !**

---

### 8.3 Exemples de commandes supplémentaires

Vous pouvez continuer à interagir avec l'agent :

| Commande                                    | Résultat                  |
| ------------------------------------------- | ------------------------- |
| "Vas-y" ou "commence"                       | Lance la campagne         |
| "Envoie les offres en anglais"              | Emails rédigés en anglais |
| "Ajoute une signature 'Votre équipe Auto+'" | Personnalise la signature |
| "Combien de véhicules sont disponibles ?"   | Consulte le sheet         |
| "Liste-moi les clients"                     | Affiche la liste          |

---

## ✅ Checklist de l'atelier

- [ ] Google Sheet créé avec feuilles "voitures" et "clients"
- [ ] Colonne "A_PROMOUVOIR" ajoutée aux voitures
- [ ] Workflow créé avec Chat Trigger
- [ ] AI Agent connecté avec GPT-4o
- [ ] Simple Memory configurée
- [ ] 3 Tools connectés (Sheets x2, Gmail)
- [ ] System Message rédigé
- [ ] Test réussi : "Lance la campagne"
- [ ] Emails reçus par les clients test

---

# 🎉 Félicitations !

Vous avez créé votre premier **Agent Marketing Automatisé** !

---