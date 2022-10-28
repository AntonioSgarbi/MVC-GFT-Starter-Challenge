
const baseUrl = window.location.origin;
var tableContainer;
var tableTemplate;

function removeItem(element) {
    element.parentNode.removeChild(element)
}

function cardClicked(element) {
    const item = tableTemplate.content.cloneNode(true).children[0]

    const columnLetters = item.querySelector("[table-row-letters]")
    const columnName = item.querySelector("[table-row-name]")
    const columnEmail = item.querySelector("[table-row-email]")
    const columnInput = item.querySelector("[table-row-input]")

    const inputElement = columnInput.querySelector("[input-table-row]")

    columnLetters.textContent = element.querySelector("[data-card-letters]").textContent
    columnName.textContent = element.querySelector("[data-card-name]").textContent
    columnEmail.textContent = element.querySelector("[data-card-email]").textContent

    inputElement.value = element.querySelector("[data-card-id]").textContent
    inputElement.name = 'members'

    tableContainer.append(item)
}

window.onload = function () {
    const searchInput = document.querySelector("[data-search-input]")

    const cardContainer = document.querySelector("[search-container]")
    const cardTemplate = document.querySelector("[search-template]")

    tableContainer = document.querySelector("[table-body-container]")
    tableTemplate = document.querySelector("[table-row-template]")

    function clearCardContainer() {
        while(cardContainer.firstChild) {
            cardContainer.removeChild(cardContainer.firstChild)
        }
    }

    function searchByName(name) {
        if(name) {
            fetch(`${baseUrl}/participant/name/${name}`)
                .then(res => res.json())
                .then(json => {
                    console.info(json);
                    clearCardContainer();
                    json.content.map(obj => {
                        const card = cardTemplate.content.cloneNode(true).children[0]

                        const id = card.querySelector("[data-card-id]")
                        const img = card.querySelector("[data-card-img]")
                        const letters = card.querySelector("[data-card-letters]")
                        const name = card.querySelector("[data-card-name]")
                        const email = card.querySelector("[data-card-email]")

                        id.textContent = obj.id
                        img.src = obj.imageUrl
                        letters.textContent = obj.letters
                        name.textContent = obj.name
                        email.textContent = obj.email

                        cardContainer.append(card)
                    });
                });
        } else {
            clearCardContainer();
        }
    }

    searchInput.addEventListener("input", e => {
        const value = e.target.value.toLowerCase()
        searchByName(value);
    });

    cardContainer.addEventListener("click", e => {
        console.log(e.target)
        console.log(e)
    });
}