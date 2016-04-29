package inlupp2_2016;



public abstract class Place {
	private String name;
	private String category;


	protected Place (String name) {
		this.name=name;
	}

	protected Place(String name, String category) {
		this.name=name;
		this.category=category;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {return  category;}
	
	
	public String toString(){
	
		return "Namn: " +getName()  +"kategori: " +getCategory() ;
	}
}
