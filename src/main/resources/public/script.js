let scanA = document.getElementById("scanA");
let scanB = document.getElementById("scanB");
let openExplorerA = document.getElementById("explorerButtonA");
let openExplorerB = document.getElementById("explorerButtonB");
let explorer = document.getElementById("explorer");
scanA.addEventListener("click", scan);
scanB.addEventListener("click", scan);
const apiURL = "/api/v1.0/";


openExplorerA.addEventListener("click", openExplorer);
openExplorerB.addEventListener("click", openExplorer);

function openExplorer(e){
    explorer.style.display="flex";
    explorer.dataset.field = e.target.dataset.field;
}

function scan(e){
    let volume = e.target.dataset.field;
    let path = document.getElementById(`pathSelector${volume.toUpperCase()}`).value;
    let url = `${apiURL}directory/scan/${volume}?path=${path}`;
    fetch(url, callback);
}
function callback(e){
    console.log(e);
}

