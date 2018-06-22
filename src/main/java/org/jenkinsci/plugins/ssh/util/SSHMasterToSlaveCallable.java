package org.jenkinsci.plugins.ssh.util;

import com.google.common.annotations.VisibleForTesting;
import hudson.model.TaskListener;
import java.io.IOException;
import java.util.UUID;
import jenkins.security.MasterToSlaveCallable;
import org.apache.log4j.MDC;
import org.jenkinsci.plugins.ssh.SSHService;
import org.jenkinsci.plugins.ssh.steps.BasicSSHStep;

/**
 * Base Callable for all SSH Steps.
 *
 * @author Naresh Rayapati.
 */
public abstract class SSHMasterToSlaveCallable extends MasterToSlaveCallable<Object, IOException> {

  private BasicSSHStep step;
  private TaskListener listener;
  private SSHService service;

  public SSHMasterToSlaveCallable(final BasicSSHStep step, final TaskListener listener) {
    this.step = step;
    this.listener = listener;
  }

  @Override
  public Object call() {
    MDC.put("execution.id", UUID.randomUUID().toString());
    this.service = createService();
    return execute();
  }

  @VisibleForTesting
  public SSHService createService() {
    return SSHService
        .create(step.getRemote(), step.isFailOnError(), step.isDryRun(), listener.getLogger());
  }

  protected abstract Object execute();

  public BasicSSHStep getStep() {
    return step;
  }

  public SSHService getService() {
    return service;
  }
}
