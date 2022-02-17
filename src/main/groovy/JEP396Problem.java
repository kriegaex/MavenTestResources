package de.scrum_master.spock;

/**
 * Use this annotation in order to mark Spock specifications which should be ignored before even initialising a runner
 * like {@code SpotlinTestRunner}, if it would make the spec's initialisation fail before Spock has a chance to evaluate
 * {@code @IgnoreIf}
 */
public @interface IgnoreRunnerIf {
}
