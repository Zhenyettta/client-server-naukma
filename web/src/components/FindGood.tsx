import { Button, Col, Container, Form, Row } from "react-bootstrap";

export default function Search() {
    return (
        <Container >
            <Row>
                <Col sm={4}>
                    <Form className="d-flex">
                        <Form.Control
                            type="search"
                            placeholder="Search"
                            className="me-1"
                            aria-label="Search"
                        />
                        <Button>
                            Find
                        </Button>
                    </Form>
                </Col>
            </Row>
        </Container>
    );
}
