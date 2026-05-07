const gameRoot = document.getElementById("game-root");
const controlsElement = document.getElementById("controls");
const boardElement = document.getElementById("board");
const statusElement = document.getElementById("status");
const restartButton = document.getElementById("restart-button");

if (gameRoot && controlsElement && boardElement && statusElement && restartButton) {
    const config = {
        columns: Number(gameRoot.dataset.columns),
        rows: Number(gameRoot.dataset.rows),
        connect: Number(gameRoot.dataset.connect)
    };
    const initialState = window.CONNECT_FOUR_STATE || {};

    let board = initialState.board || createBoard();
    let currentPlayer = initialState.currentPlayer || 1;
    let winner = initialState.winner || 0;
    let draw = initialState.draw || false;
    let lastMove = null;

    function createBoard() {
        return Array.from({ length: config.rows }, function () {
            return Array(config.columns).fill(0);
        });
    }

    function getStatusText() {
        if (winner !== 0) {
            return "Player " + winner + " wins";
        }

        if (draw) {
            return "Draw";
        }

        return "Player " + currentPlayer + " turn";
    }

    function render() {
        controlsElement.innerHTML = "";
        boardElement.innerHTML = "";

        controlsElement.style.gridTemplateColumns = "repeat(" + config.columns + ", var(--cell-size))";
        boardElement.style.gridTemplateColumns = "repeat(" + config.columns + ", var(--cell-size))";

        for (let column = 0; column < config.columns; column += 1) {
            const dropButton = document.createElement("button");
            dropButton.type = "button";
            dropButton.textContent = String(column + 1);
            dropButton.disabled = winner !== 0 || draw || board[0][column] !== 0;
            dropButton.addEventListener("click", function () {
                dropPiece(column);
            });
            controlsElement.appendChild(dropButton);
        }

        for (let row = 0; row < config.rows; row += 1) {
            for (let column = 0; column < config.columns; column += 1) {
                const cell = document.createElement("div");
                cell.className = "cell";

                if (board[row][column] === 1) {
                    cell.classList.add("player-1");
                }

                if (board[row][column] === 2) {
                    cell.classList.add("player-2");
                }

                if (lastMove && lastMove.row === row && lastMove.column === column) {
                    cell.classList.add("drop");
                    cell.style.setProperty("--drop-from", "-" + ((row + 1) * 100) + "%");
                }

                boardElement.appendChild(cell);
            }
        }

        statusElement.textContent = getStatusText();
        lastMove = null;
    }

    function persistState() {
        fetch("/game/state", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                board: board,
                currentPlayer: currentPlayer,
                winner: winner,
                draw: draw
            })
        }).catch(function () {});
    }

    function dropPiece(column) {
        if (winner !== 0 || draw) {
            return;
        }

        for (let row = config.rows - 1; row >= 0; row -= 1) {
            if (board[row][column] !== 0) {
                continue;
            }

            board[row][column] = currentPlayer;
            lastMove = { row: row, column: column };

            if (hasWinner(row, column, currentPlayer)) {
                winner = currentPlayer;
            } else if (isBoardFull()) {
                draw = true;
            } else {
                currentPlayer = currentPlayer === 1 ? 2 : 1;
            }

            render();
            persistState();
            return;
        }
    }

    function isBoardFull() {
        for (let row = 0; row < config.rows; row += 1) {
            for (let column = 0; column < config.columns; column += 1) {
                if (board[row][column] === 0) {
                    return false;
                }
            }
        }

        return true;
    }

    function hasWinner(row, column, player) {
        const directions = [
            [0, 1],
            [1, 0],
            [1, 1],
            [1, -1]
        ];

        for (let index = 0; index < directions.length; index += 1) {
            const direction = directions[index];
            const total =
                1 +
                countDirection(row, column, direction[0], direction[1], player) +
                countDirection(row, column, -direction[0], -direction[1], player);

            if (total >= config.connect) {
                return true;
            }
        }

        return false;
    }

    function countDirection(row, column, rowStep, columnStep, player) {
        let count = 0;
        let currentRow = row + rowStep;
        let currentColumn = column + columnStep;

        while (
            currentRow >= 0 &&
            currentRow < config.rows &&
            currentColumn >= 0 &&
            currentColumn < config.columns &&
            board[currentRow][currentColumn] === player
        ) {
            count += 1;
            currentRow += rowStep;
            currentColumn += columnStep;
        }

        return count;
    }

    restartButton.addEventListener("click", function () {
        board = createBoard();
        currentPlayer = 1;
        winner = 0;
        draw = false;
        lastMove = null;
        render();
        persistState();
    });

    render();
}
