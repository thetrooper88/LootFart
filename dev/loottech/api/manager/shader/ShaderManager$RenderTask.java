package dev.loottech.api.manager.shader;

import dev.loottech.api.manager.shader.ShaderManager;

public record ShaderManager.RenderTask(Runnable task, ShaderManager.Shader shader) {
}
