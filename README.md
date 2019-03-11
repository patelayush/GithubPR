GithubPR
------
Short Description - Fetches and displays data from the GitHub API List open PRs in the Google Repository. When a PR is selected, show the diffs for it. These are the aggregate diffs of the whole PR, not broken down by commit. They are displayed 'side-by-side' style, similar to “Split” the way Github displays diffs.

### Flow of the Application
1. When the app starts, User is taken to the list of the Google's Repositories.

2. User can select any Repository, also he can scroll down to load more Repositories (if he doesn't find the repository).

3. After selecting the Repository,

   * If the repository has open pull requests, then it shows the list of Open Pull Request.
   * Else the screen shows a message of 'no open pull requests'.

4. Similar to Repositories screen, user can scroll the Pull Requests to load more Pull Requests (if there are available).

5. Once the user selects a Pull Request, the User can view the changes in the file in a 'side-by-side' style, similar to “Split” the way Github displays diffs. File name along with the changes are loaded to show the differences.

<p align="center">![](GithubPRDemo.gif)</p>
