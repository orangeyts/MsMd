cd %home%
set path=%path%;"C:\Program Files\Git\bin\"

if exist .git (
	git clean -df
	git reset --hard
	git pull
)

if not exist .git (
	echo clone
	git clone https://githubsync:githubsync1@gitee.com/githubsync/spring-mvc-chat.git
)