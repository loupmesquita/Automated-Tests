jobs:
    - script: > 
        folder('Tools') {
            displayName('Tools')
            description('Folder formiscellaneous tools.')
        }

        job('Tools/clone-repository') {
            parameters {
                stringParam('GIT_REPOSITORY_URL', '', "Git URL of the repository to clone")
            }
            scm {
                git {
                    remote {
                        url('$GIT_REPOSITORY_URL')
                    }
                }
            }
            wrappers {
                preBuildCleanup()
            }
        }
       
        job('Tools/SEED') {
            parameters {
                stringParam('GITHUB_NAME', '', 'GitHub repository owner/repo_name (e.g.: "EpitechIT31000/chocolatine")')
                stringParam('DISPLAY_NAME', '', 'Display name for the job')
            }
            steps {
                dsl {
                    job('$DISPLAY_NAME') { 
                        triggers {
                            scm('H/1 * * * *')
                        }
                        scm {
                            github('$GITHUB_NAME')
                        }
                        wrappers {
                            preBuildCleanup()
                        }                    
                        steps {
                            shell('make fclean')
                            shell('make')
                            shell('make tests_run')
                            shell('make clean')
                        }
                    }
                }
            }
        }
