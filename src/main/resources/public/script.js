let scanA = document.getElementById("scanA");
let scanB = document.getElementById("scanB");
let spaceA = document.getElementsByClassName("spaceA")[0];
let spaceB = document.getElementsByClassName("spaceB")[0];
let explorer = document.getElementById("explorer");
scanA.addEventListener("click", scan);
scanB.addEventListener("click", scan);
const apiURL = "/api/v1.0/";


spaceA.addEventListener("click", openExplorer);
spaceB.addEventListener("click", openExplorer);

function openExplorer(e){
    //ToDo: Merken, was der letzte offene Pfad von A ud B war, falls man den explorer schließt, kann man ihn dann wieder auf machen und macht an der selben Stelle weiter
    fetchData(".");
    explorer.style.display="flex";
    explorer.dataset.field = e.target.dataset.field;
}

function scan(volume, path){
    let url = `${apiURL}directory/scan/${volume}?path=${path}`;
    fetch(url).then(response => response.json()).then((data) => {
        generateDOMfromScan(data);
    });
}

function generateDOMfromScan(data){
    let volume = data.volume;
    let contentWrapper = document.getElementsByClassName(`space${volume}`)[0].getElementsByClassName("pathContentWrapper")[0];
    contentWrapper.innerHTML = "";
    appendContent(contentWrapper, data.content);
}

function appendContent(wrapper, contentList){
    for (const child of contentList) {
        if(child.isDirectory && child.content.length > 0){
            appendContent(wrapper, child.content);
            continue;
        }

        let domElement = document.createElement("div");
        domElement.classList.add("content");
        let name = document.createElement("span");
        name.classList.add("name");
        name.innerText = child.name;
        let relPath = document.createElement("span");
        relPath.innerText = child.relativePath;
        relPath.classList.add("relPath");
        let hash= document.createElement("span");
        hash.innerText = child.hashValue;
        hash.classList.add("hash");

        domElement.appendChild(name);
        domElement.appendChild(relPath);
        domElement.appendChild(hash);
        wrapper.appendChild(domElement);
    }
}

