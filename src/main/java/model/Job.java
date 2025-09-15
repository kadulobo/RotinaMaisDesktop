package model;

import java.time.Instant;

/**
 * Representation of a job loaded from the database.
 */
public class Job {
    private long id;
    private String nome;
    private boolean ativo;
    private String handlerClass;
    private String handlerMethod;
    private boolean handlerStatic;
    private boolean runInSubprocess;
    private String workingDir;
    private String jvmArgs;
    private String envVars;
    private ConcurrencyPolicy policy = ConcurrencyPolicy.ALLOW;
    private int maxRetries;
    private int retryBackoff;
    private int timeout;
    private Instant createdAt;

    // getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public String getHandlerClass() { return handlerClass; }
    public void setHandlerClass(String handlerClass) { this.handlerClass = handlerClass; }
    public String getHandlerMethod() { return handlerMethod; }
    public void setHandlerMethod(String handlerMethod) { this.handlerMethod = handlerMethod; }
    public boolean isHandlerStatic() { return handlerStatic; }
    public void setHandlerStatic(boolean handlerStatic) { this.handlerStatic = handlerStatic; }
    public boolean isRunInSubprocess() { return runInSubprocess; }
    public void setRunInSubprocess(boolean runInSubprocess) { this.runInSubprocess = runInSubprocess; }
    public String getWorkingDir() { return workingDir; }
    public void setWorkingDir(String workingDir) { this.workingDir = workingDir; }
    public String getJvmArgs() { return jvmArgs; }
    public void setJvmArgs(String jvmArgs) { this.jvmArgs = jvmArgs; }
    public String getEnvVars() { return envVars; }
    public void setEnvVars(String envVars) { this.envVars = envVars; }
    public ConcurrencyPolicy getPolicy() { return policy; }
    public void setPolicy(ConcurrencyPolicy policy) { this.policy = policy; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public int getRetryBackoff() { return retryBackoff; }
    public void setRetryBackoff(int retryBackoff) { this.retryBackoff = retryBackoff; }
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
