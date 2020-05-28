@echo off

set VERSION="4.2.0.0.0126"

call mvn deploy:deploy-file -DgroupId=com.gsh -DartifactId=gsh-admin -Dversion=%VERSION% -Dpackaging=jar -Dfile=.\gsh-admin-%VERSION%.jar  -Durl=http://182.151.199.4:11157/repository/gsh-snapshots/ -DrepositoryId=gsh-snapshots
call mvn deploy:deploy-file -DgroupId=com.gsh -DartifactId=gsh-common -Dversion=%VERSION% -Dpackaging=jar -Dfile=.\gsh-common-%VERSION%.jar  -Durl=http://182.151.199.4:11157/repository/gsh-snapshots/ -DrepositoryId=gsh-snapshots
call mvn deploy:deploy-file -DgroupId=com.gsh -DartifactId=gsh-framework -Dversion=%VERSION% -Dpackaging=jar -Dfile=.\gsh-framework-%VERSION%.jar  -Durl=http://182.151.199.4:11157/repository/gsh-snapshots/ -DrepositoryId=gsh-snapshots
call mvn deploy:deploy-file -DgroupId=com.gsh -DartifactId=gsh-generator -Dversion=%VERSION% -Dpackaging=jar -Dfile=.\gsh-generator-%VERSION%.jar -Durl=http://182.151.199.4:11157/repository/gsh-snapshots/ -DrepositoryId=gsh-snapshots
call mvn deploy:deploy-file -DgroupId=com.gsh -DartifactId=gsh-quartz -Dversion=%VERSION% -Dpackaging=jar -Dfile=.\gsh-quartz-%VERSION%.jar  -Durl=http://182.151.199.4:11157/repository/gsh-snapshots/ -DrepositoryId=gsh-snapshots
call mvn deploy:deploy-file -DgroupId=com.gsh -DartifactId=gsh-system -Dversion=%VERSION% -Dpackaging=jar -Dfile=.\gsh-system-%VERSION%.jar  -Durl=http://182.151.199.4:11157/repository/gsh-snapshots/ -DrepositoryId=gsh-snapshots
call mvn deploy:deploy-file -DgroupId=com.gsh -DartifactId=gsh-system-accessories -Dversion=%VERSION% -Dpackaging=jar -Dfile=.\gsh-system-accessories-%VERSION%.jar  -Durl=http://182.151.199.4:11157/repository/gsh-snapshots/ -DrepositoryId=gsh-snapshots
call mvn deploy:deploy-file -DgroupId=com.gsh -DartifactId=gsh-system-datatraces -Dversion=%VERSION% -Dpackaging=jar -Dfile=.\gsh-system-datatraces-%VERSION%.jar  -Durl=http://182.151.199.4:11157/repository/gsh-snapshots/ -DrepositoryId=gsh-snapshots
call mvn deploy:deploy-file -DgroupId=com.gsh -DartifactId=gsh-system-factory -Dversion=%VERSION% -Dpackaging=jar -Dfile=.\gsh-system-factory-%VERSION%.jar  -Durl=http://182.151.199.4:11157/repository/gsh-snapshots/ -DrepositoryId=gsh-snapshots
call mvn deploy:deploy-file -DgroupId=com.gsh -DartifactId=gsh-system-sms -Dversion=%VERSION% -Dpackaging=jar -Dfile=.\gsh-system-sms-%VERSION%.jar  -Durl=http://182.151.199.4:11157/repository/gsh-snapshots/ -DrepositoryId=gsh-snapshots
call mvn deploy:deploy-file -DgroupId=com.gsh -DartifactId=gsh-system-product -Dversion=%VERSION% -Dpackaging=jar -Dfile=.\gsh-system-product-%VERSION%.jar  -Durl=http://182.151.199.4:11157/repository/gsh-snapshots/ -DrepositoryId=gsh-snapshots
call mvn deploy:deploy-file -DgroupId=com.gsh -DartifactId=gsh-system-portalet -Dversion=%VERSION% -Dpackaging=jar -Dfile=.\gsh-system-portalet-%VERSION%.jar  -Durl=http://182.151.199.4:11157/repository/gsh-snapshots/ -DrepositoryId=gsh-snapshots
call mvn deploy:deploy-file -DgroupId=com.gsh -DartifactId=gsh-parent -Dversion=%VERSION% -Dpackaging=pom -Dfile=.\gsh-parent.pom   -Durl=http://182.151.199.4:11157/repository/gsh-snapshots/ -DrepositoryId=gsh-snapshots
