# Gestor de Recordatorios
Esta aplicacion es mi proyecto final del grado superior de DAM que consiste en una aplicación móvil desarrollada en Android 
que permite gestionar recordatorios de forma eficiente, incluyendo funcionalidades como creación, edición, categorización y compartición con otros usuarios.

---
##  <u>Características</u>

- ✅ Crear, editar y eliminar recordatorios  
- 📅 Gestión de fechas y horas completas  
- 📂 Organización por categorías  
- 👥 Compartir recordatorios con otros usuarios  
- 🔔 Sistema de notificaciones asociado  
- ☁️ Persistencia de datos con Firebase (Firestore + Storage)  
---
##   Arquitectura 

El proyecto sigue el patrón **MVVM (Model - View - ViewModel)**, lo que permite:

- Separación clara de responsabilidades  
- Código más mantenible y escalable  
- Integración sencilla con Jetpack  

Tecnologías utilizadas:

-  **Jetpack Compose (Kotlin)** : UI moderna declarativa  
-  **Firebase Firestore** : Base de datos en la nube  
-  **Firebase Storage** : Almacenamiento de imágenes  
-  **Hilt** : Inyección de dependencias  
-  **Navigation Compose** : Navegación entre pantallas
-  **Firebase Authentication** : Sistema de autentificacion
-  **Firebase Crashlytics** : Analisis de crash
---
##  Pantallas principales

- 🏠 Home  
- 📝 Recordatorios  
- ➕ Crear recordatorio  
- 📂 Categorías  
- 👥 Amigos  
- 👤 Perfil  
---
## 🗃️ Modelo de datos
- _Users_: En esta colección se almacenará toda la información del usuario además de el array donde se almacenan los uid de los amigos agregados en la app y la subcolección de Reminders.
  
- _Reminders_: En esta subcolección se almacenará todos los recordatorios creados por el usuario dentro de cada documento hay un array con los uid de las personas que se le ha compartido el recordatorio junto con un campo que muestra quien es el creador del recordatorio y la categoría que tiene si tiene alguna.
  
- _Categories_: En esta colección se almacenará cada categoría con los recordatorios pertenecientes y el id del usuario que creó la categoría.
  
- _Notification_: En esta colección está la información de cada notificación que se envía de aviso al recordatorio donde está la descripción de la notificación, el usuario a quien se le va ha enviar y la fecha de creación de la notificación junto con el recordatorio del que está notificando.
---
 En el caso de querer saber más acerca del proyecto mira la carpeta __Recursos del extras en el desarrollo de la App__.
