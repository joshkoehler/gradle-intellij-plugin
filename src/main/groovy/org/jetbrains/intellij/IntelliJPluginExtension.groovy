package org.jetbrains.intellij

import java.util.concurrent.Callable

import org.jetbrains.intellij.dependency.IdeaDependency
import org.jetbrains.intellij.dependency.PluginDependency

/**
 * Configuration options for the {@link org.jetbrains.intellij.IntelliJPlugin}.
 */
class IntelliJPluginExtension {
    Object[] plugins = []
    String localPath
    String localSourcesPath
    Object version
    String type = 'IC'
    String pluginName
    String sandboxDirectory
    String intellijRepo = IntelliJPlugin.DEFAULT_INTELLIJ_REPO
    String pluginsRepo = IntelliJPlugin.DEFAULT_INTELLIJ_PLUGINS_REPO
    String jreRepo
    String alternativeIdePath
    String ideaDependencyCachePath
    boolean instrumentCode = true
    boolean updateSinceUntilBuild = true
    boolean sameSinceUntilBuild = false
    boolean downloadSources = true
    // turning it off disables configuring dependencies to intellij sdk jars automatically,
    // instead the intellij, intellijPlugin and intellijPlugins functions could be used for an explicit configuration
    boolean configureDefaultDependencies = true
    // configure extra dependency artifacts from intellij repo
    // the dependencies on them could be configured only explicitly using intellijExtra function in the dependencies block
    Object[] extraDependencies = []

    IdeaDependency ideaDependency
    private final Set<PluginDependency> pluginDependencies = new HashSet<>()

    String getType() {
        def resolvedVersion = getVersion()
        if (resolvedVersion == null) {
            return 'IC'
        }
        if (resolvedVersion.startsWith('IU-') || 'IU' == type) {
            return 'IU'
        } else if (resolvedVersion.startsWith('JPS-') || 'JPS' == type) {
            return "JPS"
        } else if (resolvedVersion.startsWith('CL-') || 'CL' == type) {
            return 'CL'
        } else if (resolvedVersion.startsWith('RD-') || 'RD' == type) {
            return 'RD'
        } else if (resolvedVersion.startsWith('MPS-') || 'MPS' == type) {
            return 'MPS'
        } else {
            return 'IC'
        }
    }

    String getVersion() {
        println "Greetings"
        if (version == null) {
            return null
        }

        def resolvedVersion
        if (version instanceof String) {
            resolvedVersion = version
        } else if (version instanceof Callable) {
            resolvedVersion = version.call()
        }

        if (resolvedVersion.startsWith('JPS-') || resolvedVersion.startsWith('MPS-')) {
            return version.substring(4)
        }
        if (resolvedVersion.startsWith('IU-') || resolvedVersion.startsWith('IC-') ||
            resolvedVersion.startsWith('RD-') || resolvedVersion.startsWith('CL-')) {
            return resolvedVersion.substring(3)
        }
        return resolvedVersion
    }

    Set<PluginDependency> getPluginDependencies() {
        return pluginDependencies
    }
}
