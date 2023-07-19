import React, {useContext} from "react"
import Button from "@mui/material/Button";
import Context from "../context/context";
import {useNavigate} from "react-router-dom";
export default function NavBar() {

    const ctx = useContext(Context)
    const navigate = useNavigate()

    function handleLogout(){
        ctx.logout()
        navigate()
    }

    return (
        <header className="header">

            <h2 className="header--title">Obywatel 2</h2>
            <div>

                <Button
                    size="small"
                    variant="contained"
                    sx={{mr:2}}
                    onClick={()=>{ctx.checkToken(); navigate("/menuCitizen")}}>
                    Menu główne
                </Button>
                    <Button
                        size="small"
                        variant="contained"
                        sx={{}}
                        onClick={handleLogout}

                    >
                        Wyloguj
                    </Button>

            </div>

        </header>
    )
}