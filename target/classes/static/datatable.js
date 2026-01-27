        document.addEventListener("DOMContentLoaded", function () {
            const searchInput = document.getElementById("table-search");
            const table = document.getElementById("datatable");
            const tableRows = table.getElementsByTagName("tr");

            searchInput.addEventListener("keyup", function () {
                const searchTerm = this.value.toLowerCase();
                
                for (let i = 1; i < tableRows.length; i++) {
                    const row = tableRows[i];
                    let found = false;

                    for (let j = 0; j < row.cells.length; j++) {
                        const cellValue = row.cells[j].textContent.toLowerCase();
                        if (cellValue.includes(searchTerm)) {
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        row.style.display = "";
                    } else {
                        row.style.display = "none";
                    }
                }
            });
        });