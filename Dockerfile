FROM debian:trixie-slim

ENV DEBIAN_FRONTEND=noninteractive

# Install system dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    bash \
    ca-certificates \
    curl \
    wget \
    unzip \
    zip \
    git \
    libc6 \
    make \
    cmake \
    ninja-build \
    && rm -rf /var/lib/apt/lists/*

# Install JDK 25 (Temurin)
RUN mkdir -p /etc/apt/keyrings \
    && curl -fsSL https://packages.adoptium.net/artifactory/api/gpg/key/public \
       -o /etc/apt/keyrings/adoptium.asc \
    && echo "deb [signed-by=/etc/apt/keyrings/adoptium.asc] https://packages.adoptium.net/artifactory/deb noble main" \
       > /etc/apt/sources.list.d/adoptium.list \
    && apt-get update \
    && apt-get install -y --no-install-recommends temurin-25-jdk \
    && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/usr/lib/jvm/temurin-25-jdk-amd64
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Android SDK configuration
ENV ANDROID_HOME=/opt/android-sdk
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH="${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools:${PATH}"

# Install Android command-line tools
ARG ANDROID_CMDLINE_TOOLS=13114758

RUN mkdir -p "${ANDROID_HOME}/cmdline-tools" \
    && wget -q \
       "https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_CMDLINE_TOOLS}_latest.zip" \
       -O /tmp/android-tools.zip \
    && unzip -q /tmp/android-tools.zip -d /tmp/android-tools \
    && mv /tmp/android-tools/cmdline-tools "${ANDROID_HOME}/cmdline-tools/latest" \
    && rm -rf /tmp/android-tools /tmp/android-tools.zip

# Accept licenses and install SDK packages
RUN yes | sdkmanager --licenses >/dev/null 2>&1 || true

RUN sdkmanager \
    "build-tools;37.0.0" \
    "platforms;android-37.0" \
    "ndk;30.0.16248370" \
    "platform-tools" \
    "cmake;3.22.1"

# Install Gradle
ARG GRADLE_VERSION=9.1.0

ENV GRADLE_HOME=/opt/gradle
ENV PATH="${GRADLE_HOME}/bin:${PATH}"

RUN wget -q \
    "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" \
    -O /tmp/gradle.zip \
    && unzip -q /tmp/gradle.zip -d /opt \
    && mv "/opt/gradle-${GRADLE_VERSION}" "${GRADLE_HOME}" \
    && rm /tmp/gradle.zip

# Use existing mounted project directory; no checkout
WORKDIR /workspace

CMD ["bash", "-c", "chmod +x ./gradlew && ./gradlew assembleLatestUniversalDebug"]
