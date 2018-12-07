cd #(home??)
set path=%path%;"C:\Program Files\Git\bin\"

if exist #(projectName??) (
    rem pause;
    cd #(projectName??)
	git clean -df
	git reset --hard
	git pull
	echo 'back parentPath, otherWise not exist will repeat execute!!!'
    cd ..
)

if not exist #(projectName??) (
    rem pause;
	echo clone
	git clone https://#(scmUser??):#(scmPwd??)@#(scmPath??)
)