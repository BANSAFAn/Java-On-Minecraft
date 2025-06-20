<div align="center">

# 🌟 Descarga de Java para Minecraft: Una Guía Completa 🌟

[![GitHub stars](https://img.shields.io/github/stars/BANSAFAn/Java-On-Minecraft?style=social)](https://github.com/BANSAFAn/Java-On-Minecraft/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/BANSAFAn/Java-On-Minecraft?style=social)](https://github.com/baneronetwo/Java-On-Minecraft/network/members)
[![GitHub issues](https://img.shields.io/github/issues/BANSAFAn/Java-On-Minecraft)](https://github.com/baneronetwo/Java-On-Minecraft/issues)
[![GitHub license](https://img.shields.io/github/license/baneronetwo/Java-On-Minecraft)](https://github.com/baneronetwo/Java-On-Minecraft/blob/main/LICENSE)

<p>Tu guía esencial para encontrar e instalar la versión correcta de Java para Minecraft</p>

</div>

## 🌐 Idiomas Disponibles

<div align="center">

<kbd>[<img title="English" alt="English" src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Flag_of_the_United_Kingdom_%281-2%29.svg/1200px-Flag_of_the_United_Kingdom_%281-2%29.svg.png" width="22">](../README.md)</kbd>
<kbd>[<img title="Ukraine" alt="Ukraine" src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/49/Flag_of_Ukraine.svg/1280px-Flag_of_Ukraine.svg.png" width="22">](README.ua.md)</kbd>
<kbd>[<img title="Russia" alt="Russia" src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Flag_of_Russia.svg/1280px-Flag_of_Russia.svg.png" width="22">](README.ru.md)</kbd>
<kbd>[<img title="Germany" alt="Germany" src="https://upload.wikimedia.org/wikipedia/en/thumb/b/ba/Flag_of_Germany.svg/640px-Flag_of_Germany.svg.png" width="22">](README.de.md)</kbd>
<kbd>[<img title="China" alt="China" src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Flag_of_the_People%27s_Republic_of_China.svg/800px-Flag_of_the_People%27s_Republic_of_China.svg.png" width="22">](README.zh.md)</kbd>
<kbd>[<img title="Poland" alt="Poland" src="https://upload.wikimedia.org/wikipedia/en/1/12/Flag_of_Poland.svg" width="22">](README.pl.md)</kbd>
<kbd>[<img title="Spain" alt="Spain" src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/Flag_of_Spain.svg/1200px-Flag_of_Spain.svg.png" width="22">](README.es.md)</kbd>

</div>

<div align="center">
<img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="java" width="100" height="100"/>
</div>

## 📋 Descripción General

Java es un componente esencial para ejecutar Minecraft, especialmente para versiones antiguas y juegos con mods. Esta guía te ayudará a encontrar la versión correcta de Java para tu experiencia de Minecraft y proporciona información en tiempo real sobre el estado de varias fuentes de descarga de Java.

## 🤔 ¿Por qué necesitas Java para Minecraft?

Minecraft está desarrollado utilizando el lenguaje de programación Java, lo que significa que necesitas un Entorno de Ejecución de Java (JRE) instalado en tu computadora para ejecutar el juego. Si bien las versiones más nuevas de Minecraft vienen con su propio entorno de ejecución Java, las versiones antiguas y muchas instancias con mods aún requieren una instalación separada de Java.

## 📥 Fuentes de Descarga de Java Recomendadas

<div align="center">

| Proveedor | Estado | Sitio Web |
|----------|--------|--------|
| AdoptOpenJDK (Adoptium) | ![Estado](https://img.shields.io/badge/estado-verificando-yellow) | [Descargar](https://adoptium.net/download/) |
| Oracle Java | ![Estado](https://img.shields.io/badge/estado-verificando-yellow) | [Descargar](https://www.oracle.com/java/technologies/) |
| Amazon Corretto | ![Estado](https://img.shields.io/badge/estado-verificando-yellow) | [Descargar](https://aws.amazon.com/corretto/) |
| Azul Zulu | ![Estado](https://img.shields.io/badge/estado-verificando-yellow) | [Descargar](https://www.azul.com/downloads/) |
| Red Hat OpenJDK | ![Estado](https://img.shields.io/badge/estado-verificando-yellow) | [Descargar](https://developers.redhat.com/products/openjdk/overview) |
| Microsoft OpenJDK | ![Estado](https://img.shields.io/badge/estado-verificando-yellow) | [Descargar](https://www.microsoft.com/openjdk) |

</div>

## 🔍 ¿Qué Versión de Java Necesitas para Minecraft?

### Minecraft Java Edition

- **Minecraft 1.17+**: Java 16 o superior
- **Minecraft 1.12 - 1.16.5**: Java 8
- **Minecraft 1.7.10 - 1.11.2**: Java 8
- **Minecraft 1.6 y anteriores**: Java 6 o Java 7

### Minecraft con Mods

- **Forge para MC 1.17+**: Java 16 o superior
- **Forge para MC 1.12 - 1.16.5**: Java 8
- **Forge para MC 1.7.10 y anteriores**: Java 8
- **Fabric para MC 1.17+**: Java 16 o superior
- **Fabric para MC 1.16.5 y anteriores**: Java 8

## 💻 Cómo Instalar Java

### Windows

1. Descarga el instalador de Java desde una de las fuentes recomendadas.
2. Ejecuta el archivo descargado y sigue las instrucciones del instalador.
3. Verifica la instalación abriendo el símbolo del sistema (cmd) y escribiendo: `java -version`

### macOS

1. Descarga el instalador de Java para macOS.
2. Abre el archivo descargado y sigue las instrucciones del instalador.
3. Verifica la instalación abriendo Terminal y escribiendo: `java -version`

### Linux

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install openjdk-17-jre # Para Java 17, ajusta según sea necesario
java -version # Para verificar la instalación
```

**Fedora/RHEL:**
```bash
sudo dnf install java-17-openjdk # Para Java 17, ajusta según sea necesario
java -version # Para verificar la instalación
```

**Arch Linux:**
```bash
sudo pacman -S jre-openjdk # Para la última versión
java -version # Para verificar la instalación
```

## 🔄 Cómo Cambiar Entre Diferentes Versiones de Java

### Windows

1. Descarga e instala las versiones de Java que necesitas.
2. Ve a Panel de Control > Sistema > Configuración avanzada del sistema > Variables de entorno.
3. Edita la variable `JAVA_HOME` para que apunte a la instalación de Java que deseas usar.
4. Edita la variable `Path` para asegurarte de que la ruta `%JAVA_HOME%\bin` esté al principio.

### macOS y Linux

Utiliza herramientas como `update-alternatives` (Linux) o `jenv` (macOS/Linux) para cambiar entre versiones de Java.

## ⚠️ Problemas Comunes y Soluciones

### Error "Java no se reconoce como un comando interno o externo"

**Solución:** Asegúrate de que Java esté correctamente instalado y que la ruta a la carpeta `bin` de Java esté en tu variable de entorno `Path`.

### Minecraft se bloquea con "Exit code: 1"

**Solución:** Esto a menudo indica un problema de compatibilidad de Java. Asegúrate de estar usando la versión correcta de Java para tu versión de Minecraft.

### Error de memoria "OutOfMemoryError"

**Solución:** Asigna más RAM a Minecraft en el lanzador. Para el lanzador oficial, ve a Instalaciones > selecciona tu perfil > Más opciones > Argumentos de JVM y ajusta el valor `-Xmx`.

## 📚 Recursos Adicionales

- [Página oficial de Java](https://www.java.com/)
- [Documentación de Minecraft](https://minecraft.net/)
- [Wiki de Minecraft](https://minecraft.fandom.com/)

## 📜 Licencia

Este proyecto está licenciado bajo la Licencia MIT - consulta el archivo [LICENSE](../LICENSE) para más detalles.

---

<div align="center">

⭐ **¡No olvides dar una estrella si encontraste útil este repositorio!** ⭐

</div>