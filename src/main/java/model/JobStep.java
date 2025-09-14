package model;

/**
 * Step definition belonging to a job.
 */
public class JobStep {
    private long id;
    private long jobId;
    private int ordem;
    private String nome;
    private boolean habilitado;
    private String handlerClass;
    private String handlerMethod;
    private boolean handlerStatic;
    private boolean runInSubprocess;
    private String workingDir;
    private String jvmArgs;
    private String envVars;
    private String parametros;
    private String condicaoExpr;
    private boolean continueOnFail;
    private int timeout;
    private int maxRetries;
    private int retryBackoff;

    // getters and setters omitted for brevity
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getJobId() { return jobId; }
    public void setJobId(long jobId) { this.jobId = jobId; }
    public int getOrdem() { return ordem; }
    public void setOrdem(int ordem) { this.ordem = ordem; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public boolean isHabilitado() { return habilitado; }
    public void setHabilitado(boolean habilitado) { this.habilitado = habilitado; }
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
    public String getParametros() { return parametros; }
    public void setParametros(String parametros) { this.parametros = parametros; }
    public String getCondicaoExpr() { return condicaoExpr; }
    public void setCondicaoExpr(String condicaoExpr) { this.condicaoExpr = condicaoExpr; }
    public boolean isContinueOnFail() { return continueOnFail; }
    public void setContinueOnFail(boolean continueOnFail) { this.continueOnFail = continueOnFail; }
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public int getRetryBackoff() { return retryBackoff; }
    public void setRetryBackoff(int retryBackoff) { this.retryBackoff = retryBackoff; }
}
