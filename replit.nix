{ pkgs }: {
    deps = [
        pkgs.jdk17_headless
        pkgs.maven
        pkgs.wget
    ];
}
