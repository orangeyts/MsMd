cd #(home??)
set path=%path%;"#(gitHome??)"

if exist #(projectName??) (
    echo 'update project'
    cd #(projectName??)
	git clean -df
	git reset --hard
	git pull
	echo 'back parentPath, otherWise not exist will repeat execute!!!'
    cd ..
)

if not exist #(projectName??) (
    echo 'init project'
	echo clone
	git clone https://#(scmUser??):#(scmPwd??)@#(scmPath??) #(projectName??)
)

mvn clean install -DskiptTests