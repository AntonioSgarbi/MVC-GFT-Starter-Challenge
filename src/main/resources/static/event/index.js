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
  return document.querySelector("[group-template]").content.cloneNode(true)
    .children[0];
}

function cloneActivityTemplate() {
  return document.querySelector("[activity-template]").content.cloneNode(true)
    .children[0];
}

function renderCreatedGroup(createdGroup) {
  groupContainer.append(createdGroup);
}

function addGroup() {
  const newGroup = cloneGroupTemplate();

  if (groupContainer.children[groupContainer.children.length - 1]) {
    const idFromLastGroup =
      groupContainer.children[groupContainer.children.length - 1].id;

    const arrayPositionFromLastGroup = idFromLastGroup.charAt(
      idFromLastGroup.length - 2
    );

    const positionToAddNew = parseInt(arrayPositionFromLastGroup) + 1;

    newGroup.id = `groups[${positionToAddNew}]`;
    newGroup.querySelector(
      "[input-group-name]"
    ).name = `groups[${positionToAddNew}].name`;
  } else {
    newGroup.id = `groups[0]`;
    newGroup.querySelector("[input-group-name]").name = `groups[0].name`;
  }

  const searchInputFromGroup = newGroup.querySelector("[input-search]");

  addListenerOnInputSearch(searchInputFromGroup);

  renderCreatedGroup(newGroup);
}

function removeCardGroup(button) {
  document.querySelector("[group-container]").removeChild(button.closest("[card]"))
}

function addListenerOnInputSearch(inputSearch) {
  inputSearch.addEventListener("input", (e) => {
    const value = e.target.value;
    const parentGroupFromSearch = e.target.closest('[wrapper-search]').querySelector('[search-container]');
    searchByName(parentGroupFromSearch, value);
  });
}

function addParticipantOnGroupTable(cardElement) {
  const parentGroup = cardElement.closest('[card-group]');
  const tableBody = parentGroup.querySelector('[table-body-container]');
  
  const newRowToAdd = document.querySelector("[members-row-template]").content.cloneNode(true).children[0];

  const lettersColumn = newRowToAdd.querySelector("[table-row-letters]");
  lettersColumn.textContent = cardElement.querySelector("[card-letters]").textContent;

  const nameColumn = newRowToAdd.querySelector("[table-row-name]");
  nameColumn.textContent = cardElement.querySelector("[card-name]").textContent;

  const inputColumn = newRowToAdd.querySelector("[table-row-input]");
  const inputElement = inputColumn.querySelector("[input-table-row]");
  inputElement.value = cardElement.querySelector("[card-id]").textContent;

  const lastTrInTableFromGroup = tableBody.children[tableBody.children.length - 1];

  if (lastTrInTableFromGroup) {
    const inputNameFromLastMemberInTable = lastTrInTableFromGroup.querySelector("[input-table-row]").name;
    const positionOnArrayFromLastMember = inputNameFromLastMemberInTable.charAt(inputNameFromLastMemberInTable.length - 2);
    inputElement.name = `${parentGroup.id}.members[${parseInt(positionOnArrayFromLastMember) + 1}]`;
  } else {
    inputElement.name = `${parentGroup.id}.members[0]`;
  }

  tableBody.append(newRowToAdd);
  clearInputSearchAndCardContainer();
}

function clearCardContainer(container) {
  while (container.firstChild) {
    container.removeChild(container.firstChild);
  }
}

function searchByName(container, name) {
  if (name) {
    fetch(`${baseUrl}/participant/name/${name}/?size=9`)
      .then((res) => res.json())
      .then((json) => {
        clearCardContainer(container);
        json.content.map((obj) => {
          const newCardMember = document
            .querySelector("[card-template]")
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

function clearInputSearchAndCardContainer() {
  document
    .querySelectorAll("[search-container]")
    .forEach((card) => clearCardContainer(card));

  document.querySelectorAll("[input-search]")
  .forEach(i => i.value = "")  
}

window.onload = function () {
  groupContainer = document.querySelector("[group-container]");

  const searchsPreLoaded = document.querySelectorAll("[input-search]");

  for (let i = 0; i < searchsPreLoaded.length; i++) {
    addListenerOnInputSearch(searchsPreLoaded[i]);
  }
};
