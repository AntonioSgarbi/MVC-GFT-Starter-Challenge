const baseUrl = window.location.origin;
let groupContainer;


function addActivity() {
  const newActivity = cloneActivityTemplate();

  const inputName = newActivity
    .querySelector("[activity-name]")
    .querySelector("input");

  const inputStart = newActivity
    .querySelector("[activity-start]")
    .querySelector("input");
  
    const inputEnd = newActivity
    .querySelector("[activity-end]")
    .querySelector("input");

  const containerActivity = document.querySelector("[activity-container]");
  let positionAfterLast = containerActivity.children.length;

  inputName.name = `activities[${positionAfterLast}].name`;
  inputStart.name = `activities[${positionAfterLast}].start`;
  inputEnd.name = `activities[${positionAfterLast}].end`;

  containerActivity.insertBefore(
    newActivity,
    containerActivity.children[positionAfterLast]
  );
}

function cloneGroupTemplate() {
    return document.querySelector("[group-template]").content.cloneNode(true).children[0];
}

function cloneActivityTemplate() {
    return document.querySelector("[activity-template]").content.cloneNode(true).children[0];
}

function renderCreatedGroup(createdGroup) {
    groupContainer.append(createdGroup);
}

function addGroup() {  
  const newGroup = cloneGroupTemplate();
  groupContainer = document.querySelector('[group-container]');
  
  const idFromLastGroup = groupContainer.children[groupContainer.children.length - 1].id;

  const arrayPositionFromLastGroup = idFromLastGroup.charAt(idFromLastGroup.length - 2);

  const positionToAddNew = parseInt(arrayPositionFromLastGroup) + 1; 

  newGroup.id = `groups[${positionToAddNew}]`;

  newGroup.querySelector("[input-group-name]").name = `groups[${positionToAddNew}].name`;

  const searchInputFromGroup = newGroup.querySelector("[input-search]");

  addListenerOnInputSearch(searchInputFromGroup);

  renderCreatedGroup(newGroup);
}

function addListenerOnInputSearch(inputSearch) {
  inputSearch.addEventListener("input", (e) => {
    const value = e.target.value;
    const parentGroupFromSearch = e.target.parentNode.parentNode.children[1];
    searchByName(parentGroupFromSearch, value);
  });
}

function addParticipantOnGroupMembers(cardElement) {
  let idElement = cardElement.querySelector("[card-id]");
  let lettersElement = cardElement.querySelector("[card-letters]");
  let nameElement = cardElement.querySelector("[card-name]");

  const tableBody = cardElement.parentNode.parentNode.parentNode.children[2]
    .querySelector("table")
    .querySelector("[table-body-container]");

  let id = idElement.textContent;
  let letters = lettersElement.textContent;
  let name = nameElement.textContent;

  const newRowToAdd = document
    .querySelector("[members-row-template]")
    .content.cloneNode(true).children[0];

  const columnLetters = newRowToAdd.querySelector("[table-row-letters]");
  columnLetters.textContent = letters;

  const columnName = newRowToAdd.querySelector("[table-row-name]");
  columnName.textContent = name;

  const columnInput = newRowToAdd.querySelector("[table-row-input]");
  const inputElement = columnInput.querySelector("[input-table-row]");
  inputElement.value = id;

  const parentGroup = cardElement.parentNode.parentNode.parentNode;

  

  const tbodyFromGroup = cardElement.parentNode.parentNode.parentNode
    .querySelector(".wrapper-table")
    .querySelector("table")
    .querySelector("tbody");

  const lastTrInTableFromGroup =
    tbodyFromGroup.children[tbodyFromGroup.children.length - 1];

  if (lastTrInTableFromGroup) {
    const inputNameFromLastMemberInTable =
      lastTrInTableFromGroup.querySelector("[input-table-row]").name;
    const positionOnArrayFromLastMember = inputNameFromLastMemberInTable.charAt(
      inputNameFromLastMemberInTable.length - 2
    );
    inputElement.name = `${parentGroup.id}.members[${
      parseInt(positionOnArrayFromLastMember) + 1
    }]`;
  } else {
    inputElement.name = `${parentGroup.id}.members[0]`;
  }

  tableBody.append(newRowToAdd);
}

function clearCardContainer(container) {
  while (container.firstChild) {
    container.removeChild(container.firstChild);
  }
}

function searchByName(container, name) {
  if (name) {
    fetch(`${baseUrl}/participant/name/${name}`)
      .then((res) => res.json())
      .then((json) => {
        clearCardContainer(container);
        json.content.map((obj) => {
          const newCardMember = document
            .querySelector("[search-template]")
            .content.cloneNode(true).children[0];

          const id = newCardMember.querySelector("[card-id]");
          const img = newCardMember.querySelector("[card-img]");
          const letters = newCardMember.querySelector("[card-letters]");
          const name = newCardMember.querySelector("[card-name]");

          id.textContent = obj.id;
          img.src = obj.imageUrl;
          letters.textContent = obj.letters;
          name.textContent = obj.name;

          container.append(newCardMember);
        });
      });
  } else {
    clearCardContainer(container);
  }
}

window.onload = function () {
  
  const searchsPreLoaded = document.querySelectorAll('[input-search]');
  console.log(searchsPreLoaded);

  for(let i =0; i < searchsPreLoaded.length; i++) {
    addListenerOnInputSearch(searchsPreLoaded[i]);
  }
};

